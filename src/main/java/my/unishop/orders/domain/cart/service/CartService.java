package my.unishop.orders.domain.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.RedisUtils;
import my.unishop.orders.domain.cart.dto.AddItemCartRequest;
import my.unishop.orders.domain.cart.dto.UpdateCartItemRequest;
import my.unishop.orders.domain.cart.entity.Cart;
import my.unishop.orders.domain.cart.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisUtils redisUtils;

    // 장바구니 조회
    public Cart getCart(String username) {
        Cart cart = redisUtils.get(username, Cart.class);
        if (cart == null) {
            cart = new Cart();
            cart.setMemberId(username);
        }
        return cart;
    }

    // 장바구니에 상품 추가
    // 판매 중이면서 재고 수량이 주문 수량보다 많은 경우에만 추가 가능
    public Cart addCart(String username, AddItemCartRequest addItemCartRequest) {
        if (!"SELL".equals(addItemCartRequest.getItemSellStatus())) {
            throw new IllegalArgumentException("해당 상품은 판매 중이 아닙니다.");
        }

        if (addItemCartRequest.getQuantity() > addItemCartRequest.getStockQuantity()) {
            throw new IllegalArgumentException("주문하려는 수량이 재고 수량보다 많습니다.");
        }

        Cart cart = redisUtils.get(username, Cart.class);

        if (cart == null) {
            cart = new Cart();
            cart.setMemberId(username);
        }

        // 이전에 같은 상품이 있는지 확인
        Optional<CartItem> itemOptional = cart.getItems().stream()
                .filter(item -> item.getId().equals(addItemCartRequest.getItemId()))
                .findFirst();

        if (itemOptional.isPresent()) {
            // 같은 상품이 이미 존재하는 경우, 수량을 업데이트
            CartItem existingItem = itemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + addItemCartRequest.getQuantity());
        } else {
            // 같은 상품이 없는 경우, 새로운 상품을 장바구니에 추가
            CartItem newItem = new CartItem();
            newItem.setId(addItemCartRequest.getItemId());
            newItem.setItemName(addItemCartRequest.getItemName());
            newItem.setPrice(addItemCartRequest.getPrice());
            newItem.setQuantity(addItemCartRequest.getQuantity());
            cart.getItems().add(newItem);
        }

        redisUtils.put(username, cart);
        return cart;
    }

    // 장바구니에서 특정 상품 제거
    public Cart removeItem(String username, Long itemId) {
        Cart cart = redisUtils.get(username, Cart.class);

        if (cart != null) {
            cart.getItems().removeIf(item -> item.getId().equals(itemId));
            redisUtils.put(username, cart);
        }

        return cart;
    }

    // 장바구니 초기화
    public void clearCart(String username) {
        redisUtils.deleteData(username);
    }

    // 장바구니 상품 수량 업데이트
    // 현재 재고보다 많은 수량으로 업데이트하는 경우에는 업데이트 불가
    public Cart updateCartItem(String username, UpdateCartItemRequest updateCartItemRequest) {
        Cart cart = redisUtils.get(username, Cart.class);

        if (cart != null) {
            Optional<CartItem> itemOptional = cart.getItems().stream()
                    .filter(item -> item.getId().equals(updateCartItemRequest.getItemId()))
                    .findFirst();

            if (itemOptional.isPresent()) {
                CartItem existingItem = itemOptional.get();
                if (updateCartItemRequest.getQuantity() > existingItem.getQuantity()) {
                    throw new IllegalArgumentException("재고 수량보다 많은 수량을 담을 수 없습니다.");
                }
                existingItem.setQuantity(updateCartItemRequest.getQuantity());
                redisUtils.put(username, cart);
            }
        }

        return cart;
    }
}
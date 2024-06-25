package my.unishop.orders.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.orders.domain.cart.dto.UpdateCartItemRequest;
import my.unishop.orders.domain.cart.entity.Cart;
import my.unishop.orders.domain.cart.dto.AddItemCartRequest;
import my.unishop.orders.domain.cart.service.CartService;
import my.unishop.common.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        // 헤더의 토큰에서 username 추출
        String username = getUsernameFromToken(request);
        Cart cart = cartService.getCart(username);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<Cart> addItemToCart(HttpServletRequest request, @RequestBody AddItemCartRequest addItemCartRequest) {
        String username = getUsernameFromToken(request);
        Cart cart = cartService.addCart(username, addItemCartRequest);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart/remove/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(HttpServletRequest request, @PathVariable Long itemId) {
        String username = getUsernameFromToken(request);
        Cart cart = cartService.removeItem(username, itemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        cartService.clearCart(username);
        return ResponseEntity.ok("모든 상품이 장바구니에서 삭제되었습니다.");
    }

    @PutMapping("/cart/update/{itemId}")
    public ResponseEntity<Cart> updateItemQuantity(HttpServletRequest request,
                                                   @PathVariable Long itemId,
                                                   @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        String username = getUsernameFromToken(request);
        Cart cart = cartService.updateCartItem(username, updateCartItemRequest);
        return ResponseEntity.ok(cart);
    }

    // 헤더에서 토큰을 추출하여 username 반환
    private String getUsernameFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.getUsernameFromToken(token);
    }
}
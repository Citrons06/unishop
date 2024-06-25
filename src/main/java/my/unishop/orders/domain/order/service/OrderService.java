package my.unishop.orders.domain.order.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.JwtUtil;
import my.unishop.orders.domain.order.dto.OrderRequestDto;
import my.unishop.orders.domain.order.dto.OrderResponseDto;
import my.unishop.orders.domain.order.entity.Order;
import my.unishop.orders.domain.order.entity.OrderItem;
import my.unishop.orders.domain.order.entity.OrderStatus;
import my.unishop.orders.domain.order.repository.OrderRepository;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.entity.ItemSellStatus;
import my.unishop.product.domain.item.repository.ItemRepository;
import my.unishop.product.domain.item.service.ItemService;
import my.unishop.user.domain.member.entity.Address;
import my.unishop.user.domain.member.entity.Member;
import my.unishop.user.domain.member.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static my.unishop.orders.domain.order.entity.OrderStatus.ORDERED;
import static my.unishop.orders.domain.order.entity.OrderStatus.RETURN_REQUESTED;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 상품 상태 == SELL, 0 < 주문 수량 <= 재고 수량
     * 상품 재고 == 0: SOLD_OUT
     */

    // 주문 생성
    public OrderResponseDto order(String username, OrderRequestDto orderRequestDto) {
        // 회원, 상품 정보 가져오기
        Member member = memberRepository.findByUsername(username);
        Item item = itemRepository.findById(orderRequestDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (item.getItemSellStatus() == ItemSellStatus.SELL) {
            throw new IllegalArgumentException("해당 상품은 판매 중이 아닙니다.");
        }

        if (orderRequestDto.getQuantity() > item.getQuantity()) {
            throw new IllegalArgumentException("주문 수량이 재고 수량을 초과합니다.");
        }

        // 주문 생성
        Order order = Order.builder()
                .order_username(username)
                .orderAddress(new Address(orderRequestDto.getCity(), orderRequestDto.getStreet(), orderRequestDto.getZipcode()))
                .order_tel(orderRequestDto.getOrder_tel())
                .user(member)
                .build();

        // 재고 감소
        itemService.updateItemStock(item.getId(), item.getQuantity() - orderRequestDto.getQuantity());

        orderRepository.save(order);

        // OrderResponseDto 생성
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderId(order.getId());
        responseDto.setItemId(item.getId());
        responseDto.setItemName(item.getItemName());
        responseDto.setPrice(item.getPrice());
        responseDto.setQuantity(orderRequestDto.getQuantity());
        responseDto.setItemSellStatus(item.getItemSellStatus());
        responseDto.setStockQuantity(item.getQuantity() - orderRequestDto.getQuantity());

        return responseDto;
    }

    // 주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getOrderStatus() == OrderStatus.CANCEL) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERING) {
            throw new IllegalStateException("배송 중인 상품은 취소가 불가능합니다.");
        }
        if (LocalDateTime.now().isAfter(order.getOrderDate().plusDays(1))) {
            throw new IllegalStateException("주문 완료 후 1일 이내에만 취소가 가능합니다.");
        }

        // update CANCEL
        order.cancel();

        // 재고 증가
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            itemService.updateItemStock(item.getId(), item.getQuantity() + orderItem.getCount());
        }
    }

    // 반품
    public void returnOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 반품 조건을 충족하면 update RETURN_REQUESTED
        order.returnOrder();
    }

    // 반품 처리 되었던 상품 재고 회복
    @Scheduled(fixedRate = 60 * 1000) // 1분마다 실행
    public void updateStockQuantity() {
        orderRepository.findByOrderStatusAndReturnRequestDateBefore(RETURN_REQUESTED, LocalDateTime.now().minusDays(3))
                .forEach(order -> {
                    order.completeReturn();
                    for (OrderItem orderItem : order.getOrderItems()) {
                        Item item = orderItem.getItem();
                        itemService.updateItemStock(item.getId(), item.getQuantity() + orderItem.getCount());
                    }
                    order.completeReturn();
                    orderRepository.save(order);
                });
    }

    // 주문 상태 업데이트
    @Scheduled(fixedRate = 60 * 1000) // 1분마다 실행
    public void updateOrderStatus() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.ORDERED);

        for (Order order : orders) {
            if (order.getOrderDate().plusDays(1).isBefore(LocalDateTime.now())) {
                order.setOrderStatus(OrderStatus.DELIVERING);
            }
            if (order.getOrderDate().plusDays(2).isBefore(LocalDateTime.now())) {
                order.completeDelivery();
            }
            orderRepository.save(order);
        }
    }

    /*private MemberOrderResponseDto getMemberByUsername(String username) {
        String memberServiceUrl = "http://localhost:8081/api/member/" + username;
        return restTemplate.getForObject(memberServiceUrl, MemberOrderResponseDto.class);
    }

    private ItemOrderResponseDto getItemById(Long itemId) {
        String itemServiceUrl = "http://localhost:8082/api/item/" + itemId;
        return restTemplate.getForObject(itemServiceUrl, ItemOrderResponseDto.class);
    }

    private void updateItemStock(Long itemId, int newStockQuantity) {
        String itemServiceUrl = "http://localhost:8082/api/item/update/" + itemId;
        restTemplate.put(itemServiceUrl, newStockQuantity);
    }*/

    private String getUsernameFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.getUsernameFromToken(token);
    }

}

package my.unishop.orders.domain.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.JwtUtil;
import my.unishop.orders.domain.order.dto.OrderRequestDto;
import my.unishop.orders.domain.order.dto.OrderResponseDto;
import my.unishop.orders.domain.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDto> createOrder(HttpServletRequest request, @RequestBody OrderRequestDto orderRequestDto) {
        String username = getUsernameFromToken(request);

        OrderResponseDto order = orderService.order(username, orderRequestDto);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/order/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully.");
    }

    @PostMapping("/order/return/{orderId}")
    public ResponseEntity<String> returnOrder(@PathVariable Long orderId) {
        orderService.returnOrder(orderId);
        return ResponseEntity.ok("Return requested successfully.");
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.getUsernameFromToken(token);
    }
}
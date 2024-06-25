package my.unishop.orders.domain.cart.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.JwtUtil;
import my.unishop.orders.domain.cart.dto.CartResponseDto;
import my.unishop.orders.domain.cart.entity.Cart;
import my.unishop.orders.domain.cart.service.CartService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    // 장바구니 조회
    @GetMapping("/cart")
    public String getCart(HttpServletRequest request, Model model) {
        String username = getUsernameFromToken(request);
        Cart cart = cartService.getCart(username);
        CartResponseDto cartResponseDto = new CartResponseDto(cart.getMemberId(), cart.getItems());
        model.addAttribute("cart", cartResponseDto);

        return "cart/cartlist";
    }

    // 장바구니 -> 주문 화면으로 이동
    @GetMapping("/cart/order")
    public String cartOrder(HttpServletRequest request, Model model) {
        String username = getUsernameFromToken(request);
        Cart cart = cartService.getCart(username);
        CartResponseDto cartResponseDto = new CartResponseDto(cart.getMemberId(), cart.getItems());
        model.addAttribute("cart", cartResponseDto);

        return "order/orderConfirm";
    }

    // 헤더에서 토큰을 추출하여 username 반환
    private String getUsernameFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.getUsernameFromToken(token);
    }
}
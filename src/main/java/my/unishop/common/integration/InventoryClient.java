package my.unishop.common.integration;

import lombok.RequiredArgsConstructor;
import my.unishop.orders.domain.inventory.dto.InventoryResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;
    private static final String INVENTORY_SERVICE_URL = "http://localhost:8080/api/inventory";

    public InventoryResponseDto getInventory(Long itemId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<InventoryResponseDto> response = restTemplate.exchange(
                INVENTORY_SERVICE_URL + "/" + itemId,
                HttpMethod.GET,
                entity,
                InventoryResponseDto.class
        );

        return response.getBody();
    }
}
package com.order_system_client.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;

    public String getProductInfo(Long productId) {
        return productClient.getProductById(productId);
    }

    public String getOrder(Long orderId) {

        if (orderId.equals(1L)) {
            Long productId = 100L;
            String productInfo = getProductInfo(productId);
            return "OrderID: " + orderId + ", Product Info: [" + productInfo + "]";
        }
        return "Not Valid Order";
    }

}

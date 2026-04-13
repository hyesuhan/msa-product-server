package com.order_system_client.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{productId}")
    ProductResponse getProduct(@PathVariable("productId") Long productId);

    @PostMapping("/products/{productId}/stock")
    void decreaseStock(@PathVariable("productId") Long productId, @RequestBody StockUpdateRequest request);
}

package com.order_system_client.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    // 현재 Ribbon 사용 중, 실제 서비스는 어떤 포트를 사용하는지 같이 보여줄 것이다.
    @Value("${server.port}")
    private String port;

    @GetMapping("/{productId}")
    public String getProduct(@PathVariable("productId") Long productId) {
        return "ProductID: " + productId + ", Current Port: " + port;
    }
}

package com.order_system_client.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<?> getAllProducts() {

    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductDto dto) {

    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, @RequestBody UpdateProductDto dto) {

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {

    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<?> updateProductStock(@PathVariable("productId") Long productId, @RequestBody UpdateStockDto dto) {

    }
}

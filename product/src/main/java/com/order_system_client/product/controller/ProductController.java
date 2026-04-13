package com.order_system_client.product.controller;

import com.order_system_client.product.dto.ProductRequest;
import com.order_system_client.product.dto.ProductResponse;
import com.order_system_client.product.dto.StockUpdateRequest;
import com.order_system_client.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    // 현재 Ribbon 사용 중, 실제 서비스는 어떤 포트를 사용하는지 같이 보여줄 것이다.
    @Value("${server.port}")
    private String port;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId) {
        log.info("[getProduct]Current Product-App Port is" + port);
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        log.info("[getAllProduct]Current Product-App Port is" + port);
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest dto) {
        log.info("[createProduct]Current Product-App Port is" + port);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));

    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, @RequestBody @Valid ProductRequest dto) {
        log.info("[updateProduct]Current Product-App Port is" + port);
        return ResponseEntity.ok(productService.updateProduct(productId, dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("[deleteProduct]Current Product-App Port is" + port);
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/stock")
    public ResponseEntity<?> updateProductStock(@PathVariable("productId") Long productId, @RequestBody @Valid StockUpdateRequest dto) {
        log.info("[decreaseProduct]Current Product-App Port is" + port);
        productService.decreaseStock(productId, dto);
        return ResponseEntity.ok().build();
    }
}

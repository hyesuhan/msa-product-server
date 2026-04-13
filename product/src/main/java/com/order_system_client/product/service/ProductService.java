package com.order_system_client.product.service;

import com.order_system_client.product.dto.ProductRequest;
import com.order_system_client.product.dto.ProductResponse;
import com.order_system_client.product.dto.StockUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse getProduct(Long id);
    Page<ProductResponse> getProducts(Pageable pageable);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    void decreaseStock(Long id, StockUpdateRequest request);
}

package com.order_system_client.product.service;

import com.order_system_client.product.dto.ProductFallbackResponse;
import com.order_system_client.product.entity.Product;
import com.order_system_client.product.repository.ProductRepository;
import com.order_system_client.product.dto.ProductRequest;
import com.order_system_client.product.dto.ProductResponse;
import com.order_system_client.product.dto.StockUpdateRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    public ProductResponse getProduct(Long id) {
        log.info("Fetching product details for productId: {}", id);
        Product product = findProductOrThrow(id);
        return ProductResponse.from(product);
    }

    public ProductFallbackResponse getProductFallback(Long id, Throwable throwable) {

        return ProductFallbackResponse.of(id, "상품 정보를 가져오는 데 실패했습니다. 잠시 후 다시 시도해주세요.");
    }

    @Override
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponse::from);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.create(request.getName(),request.getPrice(), request.getStock());

        return ProductResponse.from(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);
        product.update(request.getName(),request.getPrice());
        return ProductResponse.from(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public void decreaseStock(Long id, StockUpdateRequest request) {
        findProductOrThrow(id);

        int updated = productRepository.decreaseStock(id, request.getQuantity());

        if (updated == 0) throw new IllegalStateException("재고가 부족합니다.");
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. id=" + id));
    }
}

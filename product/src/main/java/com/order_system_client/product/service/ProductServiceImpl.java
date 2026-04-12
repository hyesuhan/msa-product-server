package com.order_system_client.product.service;

import com.order_system_client.product.Product;
import com.order_system_client.product.ProductRepository;
import com.order_system_client.product.dto.ProductRequest;
import com.order_system_client.product.dto.ProductResponse;
import com.order_system_client.product.dto.StockUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = findProductOrThrow(id);
        return ProductResponse.from(product);
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

package com.order_system_client.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private int stock;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Product create(String name, Long price, int stock) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.stock = stock;
        return product;
    }

    public void update(String name,Long price) {
        this.name = name;
        this.price = price;
    }

    public void validStock(int quantity) {
        if(this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
    }
}

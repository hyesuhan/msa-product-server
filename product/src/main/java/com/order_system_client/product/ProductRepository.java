package com.order_system_client.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :qty " +
            "WHERE p.id = :id AND p.stock >= :qty")
    int decreaseStock(Long id, int quantity);
}

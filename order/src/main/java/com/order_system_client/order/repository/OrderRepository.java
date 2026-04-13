package com.order_system_client.order.repository;

import com.order_system_client.order.entity.OrderStatus;
import com.order_system_client.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}

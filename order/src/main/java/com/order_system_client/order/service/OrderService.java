package com.order_system_client.order.service;

import com.order_system_client.order.dto.OrderRequest;
import com.order_system_client.order.dto.OrderResponse;
import com.order_system_client.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrder(Long orderId);
    Page<OrderResponse> getOrders(OrderStatus status, Pageable pageable);
}

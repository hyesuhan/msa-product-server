package com.order_system_client.order.service;

import com.order_system_client.order.client.ProductClient;
import com.order_system_client.order.client.ProductResponse;
import com.order_system_client.order.client.StockUpdateRequest;
import com.order_system_client.order.dto.OrderRequest;
import com.order_system_client.order.dto.OrderResponse;
import com.order_system_client.order.entity.Order;
import com.order_system_client.order.entity.OrderStatus;
import com.order_system_client.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        ProductResponse product = productClient.getProduct(request.getProductId());

        productClient.decreaseStock(request.getProductId(), new StockUpdateRequest(request.getQuantity()));

        Order order = Order.create(request.getProductId(), request.getQuantity(), product.getPrice());

        orderRepository.save(order);
        return OrderResponse.of(order, product.getName());
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);

        ProductResponse product = productClient.getProduct(order.getProductId());
        return OrderResponse.of(order, product.getName());
    }

    @Override
    public Page<OrderResponse> getOrders(OrderStatus status, Pageable pageable) {
        Page<Order> orders = (status != null)
                ? orderRepository.findByStatus(status, pageable)
                : orderRepository.findAll(pageable);

        return orders.map(order -> {
            ProductResponse product =
                    productClient.getProduct(order.getProductId());
            return OrderResponse.of(order, product.getName());
        });
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

}

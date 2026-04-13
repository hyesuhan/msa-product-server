package com.order_system_client.order.dto;

import com.order_system_client.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private Long totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public static OrderResponse of(Order order, String productName) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                productName,
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}

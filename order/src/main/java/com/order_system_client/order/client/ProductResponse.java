package com.order_system_client.order.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private int stock;
}

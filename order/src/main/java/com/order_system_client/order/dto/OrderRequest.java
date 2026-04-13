package com.order_system_client.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull @Min(1)
    private Integer quantity;
}

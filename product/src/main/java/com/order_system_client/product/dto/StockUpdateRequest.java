package com.order_system_client.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockUpdateRequest {

    @NotNull @Min(1)
    private Integer quantity;
}

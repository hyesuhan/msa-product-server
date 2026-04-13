package com.order_system_client.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @NotNull
    @Min(0)
    private Long price;

    @NotNull @Min(0)
    private Integer stock;

}

package com.order_system_client.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @NotEmpty(message = "상품 가격은 0 이상이어야 합니다.") @Min(0)
    private Long price;

    @NotEmpty(message = "재고는 0개 이상이어야 합니다.") @Min(0)
    private Integer stock;

}

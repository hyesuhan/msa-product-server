package com.order_system_client.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductFallbackResponse {

    private Long id;
    private String errorMessage;

        public static ProductFallbackResponse of(Long id, String errorMessage) {
            return new ProductFallbackResponse(id, errorMessage);
        }
}

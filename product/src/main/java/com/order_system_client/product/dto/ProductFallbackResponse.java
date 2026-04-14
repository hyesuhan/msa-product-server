package com.order_system_client.product.dto;

import lombok.Getter;

@Getter
public class ProductFallbackResponse extends ProductResponse {


    public ProductFallbackResponse(Long id) {
        super(id, "존재하지 않는 상품입니다..", null, 0, null);
    }
}

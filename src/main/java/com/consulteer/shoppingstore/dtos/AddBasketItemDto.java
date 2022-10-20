package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddBasketItemDto {
    private final Long basketId;
    private final Long productId;
    private final Integer quantity;
}

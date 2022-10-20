package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RemoveBasketItemDto {
    private final Long basketId;
    private final Long productId;
}
package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BasketItemDto {
    private final String name;
    private final Double price;
    private final Integer quantity;
}

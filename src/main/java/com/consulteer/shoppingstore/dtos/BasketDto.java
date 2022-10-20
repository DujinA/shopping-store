package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BasketDto {
    private final Double totalPrice;
    private final Integer totalItemsCount;
    private final List<BasketItemDto> basketItems;
}

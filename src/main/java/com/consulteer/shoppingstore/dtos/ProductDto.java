package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDto {
    private final String name;
    private final String description;
    private final Double unitPrice;
    private final Integer unitsInStock;
}

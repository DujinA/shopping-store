package com.consulteer.shoppingstore.dtos;

public record OrderItemDto(String name,
                           Double price,
                           Integer quantity) {
}

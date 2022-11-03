package com.consulteer.shoppingstore.dtos;

public record ProductDto(String name,
                         String description,
                         Double unitPrice,
                         Integer unitsInStock) {
}

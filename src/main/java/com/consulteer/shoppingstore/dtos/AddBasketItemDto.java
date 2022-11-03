package com.consulteer.shoppingstore.dtos;

public record AddBasketItemDto(Long basketId,
                               Long productId,
                               Integer quantity) {
}

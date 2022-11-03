package com.consulteer.shoppingstore.dtos;

import java.util.List;

public record BasketDto(Double totalPrice,
                        Integer totalItemsCount,
                        List<BasketItemDto> basketItems) {
}

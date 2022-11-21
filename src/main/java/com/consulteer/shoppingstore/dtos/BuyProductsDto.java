package com.consulteer.shoppingstore.dtos;

public record BuyProductsDto(Long basketId,
                             String country,
                             String city,
                             String address) {
}

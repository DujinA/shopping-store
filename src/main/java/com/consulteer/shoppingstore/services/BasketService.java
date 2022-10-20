package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.AddBasketItemDto;
import com.consulteer.shoppingstore.dtos.RemoveBasketItemDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;

public interface BasketService {
    BasketDto getBasketById(Long basketId);

    BasketDto getBasketByUserId(Long userId);

    ApiResponse addProductToBasket(AddBasketItemDto addBasketItemDto);

    ApiResponse removeProductFromBasket(RemoveBasketItemDto removeBasketItemDto);

    ApiResponse clearBasket(Long basketId);

}

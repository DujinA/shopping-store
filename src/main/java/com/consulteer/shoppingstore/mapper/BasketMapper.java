package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.BasketItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BasketMapper {
    private final BasketItemMapper basketItemMapper;

    public BasketDto convert(Basket basket) {
        List<BasketItemDto> basketItemsDto = basket.getBasketItems()
                .stream()
                .map(basketItemMapper::convert).toList();

        return new BasketDto(basket.getTotalPrice(),
                basket.getTotalItemsCount(),
                basketItemsDto);
    }
}

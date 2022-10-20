package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.BasketItem;
import com.consulteer.shoppingstore.dtos.BasketItemDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class BasketItemMapper {

    public BasketItemDto convert(BasketItem basketItem) {
        return new BasketItemDto(basketItem.getName(),
                basketItem.getPrice(),
                basketItem.getQuantity());
    }
}

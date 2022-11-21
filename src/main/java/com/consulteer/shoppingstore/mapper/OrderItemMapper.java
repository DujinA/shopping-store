package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.OrderItem;
import com.consulteer.shoppingstore.dtos.OrderItemDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrderItemMapper {
    public OrderItemDto convert(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getName(),
                orderItem.getPrice(),
                orderItem.getQuantity());
    }
}

package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.Order;
import com.consulteer.shoppingstore.dtos.OrderDto;
import com.consulteer.shoppingstore.dtos.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderDto convert(Order order) {
        List<OrderItemDto> orderItemsDto = order.getOrderItems()
                .stream()
                .map(orderItemMapper::convert)
                .toList();

        return new OrderDto(order.getTotalPrice(),
                order.getTotalItemsCount(),
                order.getCountry(),
                order.getCity(),
                order.getAddress(),
                order.getCreatedAt(),
                orderItemsDto);
    }
}

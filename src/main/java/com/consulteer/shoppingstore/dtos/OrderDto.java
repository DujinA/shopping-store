package com.consulteer.shoppingstore.dtos;

import java.util.Date;
import java.util.List;

public record OrderDto(Double totalPrice,
                       Integer totalItemsCount,
                       String country,
                       String city,
                       String address,
                       Date createdAt,
                       List<OrderItemDto> orderItems) {
}

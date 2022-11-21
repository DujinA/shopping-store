package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface OrderService {
    Page<OrderDto> getAllOrders(Pageable page);

    Page<OrderDto> getAllOrdersByUserId(Long userId, Pageable page);

    Page<OrderDto> getAllOrdersByUserIdAndTimeInterval(Long userId, Pageable page, Date from, Date to);

    OrderDto getOrderById(Long orderId);
}

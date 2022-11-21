package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Order;
import com.consulteer.shoppingstore.dtos.OrderDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.OrderMapper;
import com.consulteer.shoppingstore.repositories.OrderRepository;
import com.consulteer.shoppingstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDto> getAllOrders(Pageable page) {
        return orderRepository.findAll(page)
                .map(orderMapper::convert);
    }

    @Override
    public Page<OrderDto> getAllOrdersByUserId(Long userId, Pageable page) {
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId, page)
                .map(orderMapper::convert);
    }

    @Override
    public Page<OrderDto> getAllOrdersByUserIdAndTimeInterval(Long userId, Pageable page, Date from, Date to) {
        return orderRepository.findAllByUserIdAndTimeInterval(userId, page, from, to)
                .map(orderMapper::convert);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with " + orderId + " id"));

        return orderMapper.convert(order);
    }
}

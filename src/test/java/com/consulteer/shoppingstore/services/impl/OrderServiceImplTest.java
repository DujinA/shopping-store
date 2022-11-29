package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Order;
import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.OrderDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.OrderMapper;
import com.consulteer.shoppingstore.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl underTest;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    private static OrderDto orderDto;
    private static OrderDto orderBDto;
    private static PageRequest page;
    private static PageImpl<Order> pagedResponse;
    private static Order order;
    private static Order orderB;

    @BeforeEach
    void setUp() {
        orderDto = new OrderDto(10.0,
                1,
                "Srbija",
                "Novi Sad",
                "Bulevar Oslobodjenja 10",
                new Date(),
                new ArrayList<>());
        orderBDto = new OrderDto(20.0,
                2,
                "Srbija",
                "Novi Sad",
                "Bulevar Oslobodjenja 20",
                new Date(),
                new ArrayList<>());
        order = new Order();
        orderB = new Order();
        pagedResponse = new PageImpl<>(List.of(order, orderB));
        page = PageRequest.of(0, 10);
    }

    @Test
    void shouldGetAllOrders() {
        //given
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);
        when(orderMapper.convert(order)).thenReturn(orderDto);
        when(orderMapper.convert(orderB)).thenReturn(orderBDto);

        //when
        var result = underTest.getAllOrders(page);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).totalItemsCount());
        assertEquals(2, result.getContent().get(1).totalItemsCount());

        verify(orderRepository, times(1)).findAll(any(Pageable.class));
        verify(orderMapper, times(2)).convert(any());
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void shouldGetAllOrdersByUserId() {
        //given
        var user = new User();
        user.setOrders(Set.of(order, orderB));

        when(orderRepository.findAllByUserIdOrderByCreatedAtDesc(anyLong(), any(Pageable.class))).thenReturn(pagedResponse);
        when(orderMapper.convert(order)).thenReturn(orderDto);
        when(orderMapper.convert(orderB)).thenReturn(orderBDto);

        //when
        var result = underTest.getAllOrdersByUserId(1L, page);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).totalItemsCount());
        assertEquals(2, result.getContent().get(1).totalItemsCount());

        verify(orderRepository, times(1)).findAllByUserIdOrderByCreatedAtDesc(anyLong(), any(Pageable.class));
        verify(orderMapper, times(2)).convert(any());
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void shouldGetAllOrdersByUserIdAndTimeInterval() {
        //given
        var user = new User();
        user.setOrders(Set.of(order, orderB));
        var from = new Date();
        var to = new Date();

        when(orderRepository.findAllByUserIdAndTimeInterval(anyLong(), any(Pageable.class), any(), any())).thenReturn(pagedResponse);
        when(orderMapper.convert(order)).thenReturn(orderDto);
        when(orderMapper.convert(orderB)).thenReturn(orderBDto);

        //when
        var result = underTest.getAllOrdersByUserIdAndTimeInterval(1L, page, from, to);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).totalItemsCount());
        assertEquals(2, result.getContent().get(1).totalItemsCount());

        verify(orderRepository, times(1)).findAllByUserIdAndTimeInterval(anyLong(), any(Pageable.class), any(), any());
        verify(orderMapper, times(2)).convert(any());
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void shouldGetOrderById() {
        //given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.convert(any())).thenReturn(orderDto);

        //when
        var result = underTest.getOrderById(1L);

        //then
        assertNotNull(result);
        assertEquals(1, result.totalItemsCount());
        assertEquals(10.0, result.totalPrice());

        verify(orderRepository, times(1)).findById(anyLong());
        verify(orderMapper, times(1)).convert(any());
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundWhileGetOrderById() {
        //given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getOrderById(anyLong()));

        verify(orderRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(orderRepository);
    }
}
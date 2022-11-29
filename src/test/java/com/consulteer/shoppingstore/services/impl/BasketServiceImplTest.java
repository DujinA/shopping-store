package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.BasketItemDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.BasketMapper;
import com.consulteer.shoppingstore.repositories.BasketItemRepository;
import com.consulteer.shoppingstore.repositories.BasketRepository;
import com.consulteer.shoppingstore.repositories.OrderItemRepository;
import com.consulteer.shoppingstore.repositories.OrderRepository;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {
    @InjectMocks
    private BasketServiceImpl underTest;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private BasketItemRepository basketItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private BasketMapper basketMapper;
    private static Basket basket;
    private static BasketDto basketDto;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basketDto = new BasketDto(10.0,
                2,
                new ArrayList<BasketItemDto>());
    }

    @Test
    void shouldGetBasketById() {
        //given
        when(basketRepository.findById(anyLong())).thenReturn(Optional.of(basket));
        when(basketMapper.convert(any(Basket.class))).thenReturn(basketDto);

        //when
        var result = underTest.getBasketById(1L);

        //then
        assertNotNull(result);
        assertEquals(2, result.totalItemsCount());

        verify(basketRepository, times(1)).findById(anyLong());
        verify(basketMapper, times(1)).convert(any(Basket.class));
        verifyNoMoreInteractions(basketRepository);
        verifyNoMoreInteractions(basketMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBasketNotFoundWhileGetBasketById() {
        //given
        when(basketRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getBasketById(1L));

        verify(basketRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(basketRepository);
    }

    @Test
    void getBasketByUserId() {
        //given
        var user = new User();
        user.setBasket(basket);

        when(basketRepository.findByUserId(anyLong())).thenReturn(Optional.of(basket));
        when(basketMapper.convert(any(Basket.class))).thenReturn(basketDto);

        //when
        var result = underTest.getBasketByUserId(1L);

        //then
        assertNotNull(result);
        assertEquals(2, result.totalItemsCount());

        verify(basketRepository, times(1)).findByUserId(anyLong());
        verify(basketMapper, times(1)).convert(any(Basket.class));
        verifyNoMoreInteractions(basketRepository);
        verifyNoMoreInteractions(basketMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundWhileGetBasketByUserId() {
        //given
        when(basketRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getBasketByUserId(1L));

        verify(basketRepository, times(1)).findByUserId(anyLong());
        verifyNoMoreInteractions(basketRepository);
    }

    @Test
    void addProductToBasket() {
    }

    @Test
    void removeProductFromBasket() {
    }

    @Test
    void shouldClearBasket() {
        //given
        basket.setId(1L);
        when(basketRepository.findById(anyLong())).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        //when
        var result = underTest.clearBasket(1L);

        // then
        assertEquals(true, result.getSuccess());
        assertEquals("Basket cleared successfully", result.getMessage());

        verify(basketRepository, times(1)).findById(anyLong());
        verify(basketRepository, times(1)).save(any(Basket.class));
        verifyNoMoreInteractions(basketRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBasketNotFoundWhileClearBasket() {
        //given
        when(basketRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.clearBasket(1L));

        verify(basketRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(basketRepository);
    }

    @Test
    void buyProducts() {
    }
}
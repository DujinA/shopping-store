package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.BasketItem;
import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.AddBasketItemDto;
import com.consulteer.shoppingstore.dtos.BasketItemDto;
import com.consulteer.shoppingstore.dtos.RemoveBasketItemDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.BasketItemMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.BasketItemRepository;
import com.consulteer.shoppingstore.repositories.BasketRepository;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import com.consulteer.shoppingstore.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;
    private final BasketItemMapper basketItemMapper;

    @Override
    public BasketDto getBasketById(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + basketId + " id"));

        return mapBasketDto(basket);
    }

    @Override
    public BasketDto getBasketByUserId(Long userId) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + userId + " id"));

        return mapBasketDto(basket);
    }

    private BasketDto mapBasketDto(Basket basket) {
        List<BasketItemDto> basketItemsDto = basket.getBasketItems()
                .stream()
                .map(basketItemMapper::convert).toList();

        return new BasketDto(basket.getTotalPrice(),
                basket.getTotalItemsCount(),
                basketItemsDto);
    }

    @Override
    public ApiResponse addProductToBasket(AddBasketItemDto addBasketItemDto) {
        Product product = productRepository.findById(addBasketItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + addBasketItemDto.getProductId() + " id"));

        Basket basket = basketRepository.findById(addBasketItemDto.getBasketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + addBasketItemDto.getBasketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();
        BasketItem basketItem = findBasketItem(basketItems, product.getId());

        if (product.getUnitsInStock() >= addBasketItemDto.getQuantity()) {
            if (basketItems == null) {
                basketItems = new ArrayList<>();

                basketItem = createNewBasketItem(
                        addBasketItemDto.getQuantity(),
                        product,
                        basket,
                        basketItems);
            } else {
                if (basketItem == null) {
                    basketItem = createNewBasketItem(
                            addBasketItemDto.getQuantity(),
                            product,
                            basket,
                            basketItems);
                } else {
                    basketItem.setPrice(basketItem.getPrice() + product.getUnitPrice() * addBasketItemDto.getQuantity());
                    basketItem.setQuantity(basketItem.getQuantity() + addBasketItemDto.getQuantity());
                }
            }
            basketItemRepository.save(basketItem);

            basket.setBasketItems(basketItems);

            Double totalPrice = totalPrice(basket.getBasketItems());
            Integer totalItemsCount = totalItemsCounter(basket.getBasketItems());

            basket.setTotalPrice(totalPrice);
            basket.setTotalItemsCount(totalItemsCount);

            basketRepository.save(basket);

            return new ApiResponse("Product added to basket successfully", true);
        }

        return new ApiResponse("Failed attempt to add product to basket", false);
    }

    @Override
    @Transactional
    public ApiResponse removeProductFromBasket(RemoveBasketItemDto removeBasketItemDto) {
        Basket basket = basketRepository.findById(removeBasketItemDto.getBasketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + removeBasketItemDto.getBasketId() + " id"));
        Product product = productRepository.findById(removeBasketItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + removeBasketItemDto.getBasketId() + " id"));

        var basketItems = basket.getBasketItems().stream()
                .filter(b -> !Objects.equals(b.getProduct(), product))
                .collect(Collectors.toList());

        basket.setBasketItems(basketItems);
        Double totalPrice = totalPrice(basket.getBasketItems());
        Integer totalItemsCount = totalItemsCounter(basket.getBasketItems());

        basket.setTotalPrice(totalPrice);
        basket.setTotalItemsCount(totalItemsCount);
        basketRepository.save(basket);

        basketItemRepository.deleteByProductId(removeBasketItemDto.getProductId());
        return new ApiResponse("Product removed from basket successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse clearBasket(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + basketId + " id"));

        basketItemRepository.deleteAllByBasketId(basket.getId());

        return new ApiResponse("Basket cleared successfully", true);
    }

    private BasketItem createNewBasketItem(Integer quantity,
                                           Product product,
                                           Basket basket,
                                           List<BasketItem> basketItems) {
        BasketItem basketItem;

        basketItem = new BasketItem();
        basketItem.setName(product.getName());
        basketItem.setPrice(product.getUnitPrice() * quantity);
        basketItem.setQuantity(quantity);
        basketItem.setProduct(product);
        basketItem.setBasket(basket);
        basketItems.add(basketItem);

        return basketItem;
    }

    private BasketItem findBasketItem(List<BasketItem> basketItems, Long productId) {
        if (basketItems == null) {
            return null;
        }
        BasketItem basketItem = null;

        for (BasketItem item : basketItems) {
            if (Objects.equals(item.getProduct().getId(), productId)) {
                basketItem = item;
            }
        }
        return basketItem;
    }

    private Double totalPrice(List<BasketItem> basketItems) {
        Double totalPrice = 0.0;

        for (BasketItem item : basketItems) {
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

    private Integer totalItemsCounter(List<BasketItem> basketItems) {
        Integer totalItemsCount = 0;

        for (BasketItem item : basketItems) {
            totalItemsCount += item.getQuantity();
        }

        return totalItemsCount;
    }
}

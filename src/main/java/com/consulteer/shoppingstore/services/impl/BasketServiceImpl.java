package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.BasketItem;
import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.AddBasketItemDto;
import com.consulteer.shoppingstore.dtos.BasketItemDto;
import com.consulteer.shoppingstore.dtos.RemoveBasketItemDto;
import com.consulteer.shoppingstore.exceptions.BadRequestException;
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
import java.util.List;

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
        Product product = productRepository.findById(addBasketItemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + addBasketItemDto.productId() + " id"));

        Basket basket = basketRepository.findById(addBasketItemDto.basketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + addBasketItemDto.basketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();

        basketItems.stream()
                .filter(basketItem -> basketItem.getProduct().equals(product))
                .findFirst()
                .ifPresentOrElse(basketItem -> {
                    if (basketItem.getProduct().getUnitsInStock() < addBasketItemDto.quantity() + basketItem.getQuantity())
                        throw new BadRequestException("Product out of stock!");

                    basketItem.setPrice(basketItem.getPrice() + product.getUnitPrice() * addBasketItemDto.quantity());
                    basketItem.setQuantity(basketItem.getQuantity() + addBasketItemDto.quantity());

                    basketItemRepository.save(basketItem);
                }, () -> {
                    if (product.getUnitsInStock() < addBasketItemDto.quantity())
                        throw new BadRequestException("Product out of stock!");

                    BasketItem basketItem = createNewBasketItem(
                            addBasketItemDto.quantity(),
                            product,
                            basket,
                            basketItems);

                    basketItemRepository.save(basketItem);
                });

        Double totalPrice = totalPrice(basket.getBasketItems());
        Integer totalItemsCount = totalItemsCounter(basket.getBasketItems());

        basket.setTotalPrice(totalPrice);
        basket.setTotalItemsCount(totalItemsCount);

        basketRepository.save(basket);

        return new ApiResponse("Product added to basket successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse removeProductFromBasket(RemoveBasketItemDto removeBasketItemDto) {
        Basket basket = basketRepository.findById(removeBasketItemDto.basketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + removeBasketItemDto.basketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();

        basketItems.stream()
                .filter(basketItem -> basketItem.getProduct().getId().equals(removeBasketItemDto.productId()))
                .findFirst()
                .ifPresentOrElse(basketItem -> {
                            basketItems.remove(basketItem);
                            basketItemRepository.delete(basketItem);

                            Double totalPrice = totalPrice(basket.getBasketItems());
                            Integer totalItemsCount = totalItemsCounter(basket.getBasketItems());

                            basket.setTotalPrice(totalPrice);
                            basket.setTotalItemsCount(totalItemsCount);
                            basketRepository.save(basket);

                        },
                        () -> {
                            throw new BadRequestException("Product is not in the basket");
                        });

        return new ApiResponse("Product removed from basket successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse clearBasket(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + basketId + " id"));

        basketItemRepository.deleteAllByBasketId(basket.getId());

        basket.setTotalPrice(0.00);
        basket.setTotalItemsCount(0);
        basketRepository.save(basket);

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

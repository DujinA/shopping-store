package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.BasketItem;
import com.consulteer.shoppingstore.domain.Order;
import com.consulteer.shoppingstore.domain.OrderItem;
import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.AddBasketItemDto;
import com.consulteer.shoppingstore.dtos.BuyProductsDto;
import com.consulteer.shoppingstore.dtos.RemoveBasketItemDto;
import com.consulteer.shoppingstore.exceptions.BadRequestException;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.BasketMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.BasketItemRepository;
import com.consulteer.shoppingstore.repositories.BasketRepository;
import com.consulteer.shoppingstore.repositories.OrderItemRepository;
import com.consulteer.shoppingstore.repositories.OrderRepository;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import com.consulteer.shoppingstore.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BasketMapper basketMapper;

    @Override
    public BasketDto getBasketById(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + basketId + " id"));

        return basketMapper.convert(basket);
    }

    @Override
    public BasketDto getBasketByUserId(Long userId) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + userId + " id"));

        return basketMapper.convert(basket);
    }

    @Override
    @Transactional
    public ApiResponse addProductToBasket(AddBasketItemDto addBasketItemDto) {
        Product product = productRepository.findById(addBasketItemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + addBasketItemDto.productId() + " id"));

        Basket basket = basketRepository.findById(addBasketItemDto.basketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + addBasketItemDto.basketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();

        basketItems.stream()
                .filter(basketItem -> basketItem.getProduct().equals(product))
                .findAny()
                .ifPresentOrElse(basketItem -> updateBasketItem(basketItem, addBasketItemDto, product),
                        () -> addNewBasketItem(product, addBasketItemDto, basket, basketItems));

        setBasketTotalPriceAndItemsCount(basket);

        return new ApiResponse("Product added to basket successfully", true);
    }

    private void addNewBasketItem(Product product,
                                  AddBasketItemDto addBasketItemDto,
                                  Basket basket,
                                  List<BasketItem> basketItems) {
        if (product.getUnitsInStock() < addBasketItemDto.quantity()) {
            throw new BadRequestException("Product out of stock!");
        }

        createNewBasketItem(product, addBasketItemDto, basket, basketItems);
    }

    private void createNewBasketItem(Product product,
                                     AddBasketItemDto addBasketItemDto,
                                     Basket basket,
                                     List<BasketItem> basketItems) {
        BasketItem basketItem = new BasketItem();

        basketItem.setName(product.getName());
        basketItem.setPrice(product.getUnitPrice() * addBasketItemDto.quantity());
        basketItem.setQuantity(addBasketItemDto.quantity());
        basketItem.setProduct(product);
        basketItem.setBasket(basket);
        basketItemRepository.save(basketItem);

        basketItems.add(basketItem);
    }

    private void updateBasketItem(BasketItem basketItem,
                                  AddBasketItemDto addBasketItemDto,
                                  Product product) {
        if (basketItem.getProduct().getUnitsInStock() < addBasketItemDto.quantity() + basketItem.getQuantity()) {
            throw new BadRequestException("Product out of stock!");

        }

        basketItem.setPrice(basketItem.getPrice() + product.getUnitPrice() * addBasketItemDto.quantity());
        basketItem.setQuantity(basketItem.getQuantity() + addBasketItemDto.quantity());

        basketItemRepository.save(basketItem);
    }

    private void setBasketTotalPriceAndItemsCount(Basket basket) {
        Double totalPrice = totalPrice(basket.getBasketItems());
        Integer totalItemsCount = totalItemsCounter(basket.getBasketItems());

        basket.setTotalPrice(totalPrice);
        basket.setTotalItemsCount(totalItemsCount);

        basketRepository.save(basket);
    }

    @Override
    @Transactional
    public ApiResponse removeProductFromBasket(RemoveBasketItemDto removeBasketItemDto) {
        Basket basket = basketRepository.findById(removeBasketItemDto.basketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + removeBasketItemDto.basketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();

        BasketItem basketItem = basketItems.stream()
                .filter(bi -> bi.getProduct().getId().equals(removeBasketItemDto.productId()))
                .findAny()
                .orElseThrow(() -> new BadRequestException("Product is not in the basket"));

        basketItems.remove(basketItem);
        basketItemRepository.delete(basketItem);

        setBasketTotalPriceAndItemsCount(basket);

        return new ApiResponse("Product removed from basket successfully", true);
    }

    @Override
    @Transactional
    public ApiResponse clearBasket(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + basketId + " id"));

        emptyBasket(basket);

        return new ApiResponse("Basket cleared successfully", true);
    }

    private void emptyBasket(Basket basket) {
        basketItemRepository.deleteAllByBasketId(basket.getId());

        basket.setTotalPrice(0.00);
        basket.setTotalItemsCount(0);
        basketRepository.save(basket);
    }

    @Override
    @Transactional
    public ApiResponse buyProducts(BuyProductsDto buyProductsDto) {
        Basket basket = basketRepository.findById(buyProductsDto.basketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found with " + buyProductsDto.basketId() + " id"));

        List<BasketItem> basketItems = basket.getBasketItems();

        if (basketItems.size() == 0) {
            throw new BadRequestException("Basket is empty");
        }

        Order order = createNewOrder(buyProductsDto, basket);

        basketItems.forEach(basketItem -> {
            createNewOrderItem(order, basketItem);
            subtractUnitsInStock(basketItem);
        });

        emptyBasket(basket);

        return new ApiResponse("Products bought successfully", true);
    }

    private void createNewOrderItem(Order order, BasketItem basketItem) {
        OrderItem orderItem = new OrderItem();

        orderItem.setName(basketItem.getName());
        orderItem.setPrice(basketItem.getPrice());
        orderItem.setQuantity(basketItem.getQuantity());
        orderItem.setProduct(basketItem.getProduct());
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
    }

    private Order createNewOrder(BuyProductsDto buyProductsDto, Basket basket) {
        Order order = new Order();

        order.setTotalPrice(basket.getTotalPrice());
        order.setTotalItemsCount(basket.getTotalItemsCount());
        order.setUser(basket.getUser());
        order.setCountry(buyProductsDto.country());
        order.setCity(buyProductsDto.city());
        order.setAddress(buyProductsDto.address());
        orderRepository.save(order);

        return order;
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

    private void subtractUnitsInStock(BasketItem basketItem) {
        Product product = basketItem.getProduct();

        product.setUnitsInStock(product.getUnitsInStock() - basketItem.getQuantity());
        productRepository.save(product);
    }
}

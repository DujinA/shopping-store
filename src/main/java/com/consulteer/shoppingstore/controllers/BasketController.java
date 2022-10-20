package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.BasketDto;
import com.consulteer.shoppingstore.dtos.AddBasketItemDto;
import com.consulteer.shoppingstore.dtos.RemoveBasketItemDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping("/{basket-id}")
    public ResponseEntity<BasketDto> getBasketById(@PathVariable("basket-id") Long basketId) {
        return ResponseEntity.ok(basketService.getBasketById(basketId));
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<BasketDto> getBasketByUserId(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(basketService.getBasketByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProductToBasket(@RequestBody AddBasketItemDto addBasketItemDto) {
        return new ResponseEntity<>(basketService.addProductToBasket(addBasketItemDto), HttpStatus.OK);
    }

    @PutMapping("/removeProduct")
    public ResponseEntity<ApiResponse> removeProductFromBasket(@RequestBody RemoveBasketItemDto removeBasketItemDto) {
        return new ResponseEntity<>(basketService.removeProductFromBasket(removeBasketItemDto), HttpStatus.OK);
    }

    @PutMapping("/clear/{basket-id}")
    public ResponseEntity<ApiResponse> clearBasket(@PathVariable("basket-id") Long basketId) {
        return new ResponseEntity<>(basketService.clearBasket(basketId), HttpStatus.OK);
    }
}

package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long productId);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, Long productId);

    ApiResponse deleteProduct(Long productId);
}

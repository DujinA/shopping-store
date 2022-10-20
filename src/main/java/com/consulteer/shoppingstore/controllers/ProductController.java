package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping("/")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @PutMapping("/{product-id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(productService.updateProduct(productDto, productId));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("product-id") Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

}

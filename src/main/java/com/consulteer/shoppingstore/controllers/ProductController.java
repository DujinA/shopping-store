package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.dtos.ReportDto;
import com.consulteer.shoppingstore.dtos.TopProductsDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import com.consulteer.shoppingstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{product-id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(productService.updateProduct(productDto, productId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{product-id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("product-id") Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @GetMapping("/newest")
    public ResponseEntity<List<ProductDto>> findNewProducts() {
        return new ResponseEntity<>(productService.getNewProducts(), HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopProductsDto>> findTopProducts() {
        return ResponseEntity.ok(productService.getTopProducts());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{product-id}/report")
    public ResponseEntity<ReportDto> findReport(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(productService.getReport(productId));
    }
}

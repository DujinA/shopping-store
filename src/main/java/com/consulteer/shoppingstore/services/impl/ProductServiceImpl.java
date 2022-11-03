package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.ProductMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import com.consulteer.shoppingstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = findProductById(productId);

        return productMapper.convert(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + productId + " id"));
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.convert(productDto);

        productRepository.save(product);

        return productMapper.convert(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId) {
        Product updatedProduct = findProductById(productId);

        updateBasicFields(productDto, updatedProduct);
        productRepository.save(updatedProduct);

        return productMapper.convert(updatedProduct);
    }

    private static void updateBasicFields(ProductDto productDto, Product updatedProduct) {
        updatedProduct.setName(productDto.name());
        updatedProduct.setDescription(productDto.description());
        updatedProduct.setUnitPrice(productDto.unitPrice());
        updatedProduct.setUnitsInStock(productDto.unitsInStock());
    }

    @Override
    public ApiResponse deleteProduct(Long productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);

        return new ApiResponse("Product deleted successfully", true);
    }
}

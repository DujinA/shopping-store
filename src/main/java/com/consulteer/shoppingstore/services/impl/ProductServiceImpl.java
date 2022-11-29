package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.interfaces.ProductReport;
import com.consulteer.shoppingstore.dtos.CityDto;
import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.dtos.ReportDto;
import com.consulteer.shoppingstore.dtos.TopProductDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.CityMapper;
import com.consulteer.shoppingstore.mapper.ProductMapper;
import com.consulteer.shoppingstore.mapper.ReportMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import com.consulteer.shoppingstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CityMapper cityMapper;
    private final ReportMapper reportMapper;

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

    @Override
    public List<ProductDto> getNewProducts() {
        List<Product> list = productRepository.findNewProducts();
        return list.stream().map(productMapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<TopProductDto> getTopProducts() {
        List<Product> list = productRepository.findTopProducts();
        return list.stream().map(productMapper::convertTopProducts).collect(Collectors.toList());
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + productId + " id"));
    }

    @Override
    public ReportDto getReport(Long productId) {
        ProductReport productReport = productRepository.findProductNameAndPrice(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with " + productId + " id"));
        List<CityDto> cityDto = productRepository.findCitiesInfo(productId).stream()
                .map(c -> cityMapper.convert(c, productReport.getProductPrice()))
                .toList();

        return reportMapper.convert(cityDto, productReport);
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.convert(productDto);

        productRepository.save(product);

        return productMapper.convert(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto, Long productId) {
        Product updatedProduct = findProductById(productId);

        productMapper.updateBasicFields(productDto, updatedProduct);
        productRepository.save(updatedProduct);

        return productMapper.convert(updatedProduct);
    }

    @Override
    @Transactional
    public ApiResponse deleteProduct(Long productId) {
        productRepository.deleteById(productId);

        return new ApiResponse("Product deleted successfully", true);
    }
}

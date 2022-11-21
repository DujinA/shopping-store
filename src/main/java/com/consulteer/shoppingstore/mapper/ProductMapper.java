package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.dtos.TopProductsDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProductMapper {
    public ProductDto convert(Product product) {
        return new ProductDto(product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getUnitsInStock());
    }

    public Product convert(ProductDto productDto) {
        return new Product(productDto.name(),
                productDto.description(),
                productDto.unitPrice(),
                productDto.unitsInStock());
    }

    public TopProductsDto convertTopProducts(Product product) {
        return new TopProductsDto(product.getName(),
                product.getUnitPrice(),
                checkInStock(product));
    }

    private String checkInStock(Product product) {
        return product.getUnitsInStock() == 0 ? "OUT OF STOCK" : "IN STOCK";

    }
}
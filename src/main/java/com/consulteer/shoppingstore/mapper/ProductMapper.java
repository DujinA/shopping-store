package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.ProductDto;
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
}
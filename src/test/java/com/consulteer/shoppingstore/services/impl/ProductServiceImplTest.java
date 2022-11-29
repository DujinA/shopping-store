package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Product;
import com.consulteer.shoppingstore.dtos.CityDto;
import com.consulteer.shoppingstore.dtos.ProductDto;
import com.consulteer.shoppingstore.dtos.ReportDto;
import com.consulteer.shoppingstore.dtos.TopProductDto;
import com.consulteer.shoppingstore.dtos.interfaces.CityReport;
import com.consulteer.shoppingstore.dtos.interfaces.ProductReport;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.CityMapper;
import com.consulteer.shoppingstore.mapper.ProductMapper;
import com.consulteer.shoppingstore.mapper.ReportMapper;
import com.consulteer.shoppingstore.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl underTest;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CityMapper cityMapper;
    @Mock
    private ReportMapper reportMapper;
    private static Product product;
    private static ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product("product1",
                "product1 description",
                10.0,
                10);
        productDto = new ProductDto("product1",
                "product1 description",
                10.0,
                10);
    }

    @Test
    void shouldGetAllProducts() {
        //given
        var productB = new Product("product2",
                "product2 description",
                5.0,
                5);
        var productBDto = new ProductDto("product2",
                "product2 description",
                5.0,
                5);

        when(productRepository.findAll()).thenReturn(List.of(product, productB));
        when(productMapper.convert(product)).thenReturn(productDto);
        when(productMapper.convert(productB)).thenReturn(productBDto);

        //when
        var result = underTest.getAllProducts();

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("product1", result.get(0).name());
        assertEquals("product2", result.get(1).name());

        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(2)).convert(any(Product.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productMapper);
    }

    @Test
    void shouldGetProductById() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.convert(any(Product.class))).thenReturn(productDto);

        //when
        var result = underTest.getProductById(anyLong());

        //then
        assertNotNull(result);
        assertEquals("product1", result.name());
        assertEquals("product1 description", result.description());
        assertEquals(10.0, result.unitPrice());
        assertEquals(10, result.unitsInStock());

        verify(productRepository, times(1)).findById(anyLong());
        verify(productMapper, times(1)).convert(any(Product.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFoundWhileGetProductById() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getProductById(anyLong()));

        verify(productRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldGetNewProducts() {
        //given
        var productB = new Product("product2",
                "product2 description",
                5.0,
                5);
        var productBDto = new ProductDto("product2",
                "product2 description",
                5.0,
                5);

        when(productRepository.findNewProducts()).thenReturn(List.of(product, productB));
        when(productMapper.convert(product)).thenReturn(productDto);
        when(productMapper.convert(productB)).thenReturn(productBDto);

        //when
        var result = underTest.getNewProducts();

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("product1", result.get(0).name());
        assertEquals("product2", result.get(1).name());

        verify(productRepository, times(1)).findNewProducts();
        verify(productMapper, times(2)).convert(any(Product.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productMapper);
    }

    @Test
    void shouldGetTopProducts() {
        //given
        var productB = new Product("product2",
                "product2 description",
                0.0,
                0);
        var topProductDto = new TopProductDto("product1",
                10.0,
                "IN_STOCK");
        var topProductBDto = new TopProductDto("product2",
                5.0,
                "OUT_OF_STOCK");

        when(productRepository.findTopProducts()).thenReturn(List.of(product, productB));
        when(productMapper.convertTopProducts(product)).thenReturn(topProductDto);
        when(productMapper.convertTopProducts(productB)).thenReturn(topProductBDto);

        //when
        var result = underTest.getTopProducts();

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("product1", result.get(0).name());
        assertEquals("product2", result.get(1).name());

        verify(productRepository, times(1)).findTopProducts();
        verify(productMapper, times(2)).convertTopProducts(any(Product.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productMapper);
    }

    @Test
    void shouldGetReport() {
        //given
        var cityDto = new CityDto("Novi sad",
                5,
                25.0);
        var cityBDto = new CityDto("Beograd",
                2,
                10.0);
        var productReport = getProductReport();
        var cityAReport = getCityAReport();
        var cityBReport = getCityBReport();
        var reportDto = new ReportDto("reportName",
                5.0,
                List.of(cityDto, cityBDto),
                35.0);

        when(productRepository.findProductNameAndPrice(anyLong())).thenReturn(Optional.of(productReport));
        when(cityMapper.convert(any(), anyDouble())).thenReturn(cityDto);
        when(cityMapper.convert(any(), anyDouble())).thenReturn(cityBDto);
        when(reportMapper.convert(any(), any())).thenReturn(reportDto);
        when(productRepository.findCitiesInfo(anyLong())).thenReturn(List.of(cityAReport, cityBReport));

        //when
        var result = underTest.getReport(anyLong());

        //then
        assertNotNull(result);
        assertEquals("reportName", result.name());
        assertEquals(5.0, result.price());
        assertEquals(List.of(cityDto, cityBDto), result.cities());
        assertEquals(35.0, result.totalIncome());

        verify(productRepository, times(1)).findProductNameAndPrice(anyLong());
        verify(cityMapper, times(2)).convert(any(), any());
        verify(reportMapper, times(1)).convert(any(), any());
        verify(productRepository, times(1)).findCitiesInfo(anyLong());
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(cityMapper);
        verifyNoMoreInteractions(reportMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductReportNotFoundWhileGetReport() {
        //given
        when(productRepository.findProductNameAndPrice(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getReport(anyLong()));

        verify(productRepository, times(1)).findProductNameAndPrice(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldAddProduct() {
        //given
        when(productMapper.convert(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.convert(any(Product.class))).thenReturn(productDto);

        //when
        var result = underTest.addProduct(productDto);

        //then
        assertNotNull(result);
        assertEquals("product1", result.name());
        assertEquals("product1 description", result.description());
        assertEquals(10.0, result.unitPrice());
        assertEquals(10, result.unitsInStock());

        verify(productMapper, times(1)).convert(any(Product.class));
        verify(productRepository, times(1)).save(any());
        verify(productMapper, times(1)).convert(any(ProductDto.class));
        verifyNoMoreInteractions(productMapper);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldUpdateProduct() {
        //given
        var updateProductDto = new ProductDto("product2",
                "product2 description",
                5.0,
                5);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.convert(any(Product.class))).thenReturn(updateProductDto);

        //when
        var result = underTest.updateProduct(updateProductDto, anyLong());

        //then
        assertNotNull(result);
        assertEquals("product2", result.name());
        assertEquals("product2 description", result.description());
        assertEquals(5.0, result.unitPrice());
        assertEquals(5, result.unitsInStock());

        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any());
        verify(productMapper, times(1)).convert(any(Product.class));
        verify(productMapper, times(1)).updateBasicFields(any(), any());
        verifyNoMoreInteractions(productMapper);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFoundWhileUpdateProduct() {
        //given
        var updateProductDto = new ProductDto("product2",
                "product2 description",
                5.0,
                5);

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateProduct(updateProductDto, anyLong()));

        verify(productRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldDeleteProduct() {
        //given - when
        var result = underTest.deleteProduct(anyLong());

        //then
        assertEquals(true, result.getSuccess());
        assertEquals("Product deleted successfully", result.getMessage());
    }

    private ProductReport getProductReport() {
        return new ProductReport() {
            @Override
            public String getProductName() {
                return "product1";
            }

            @Override
            public Double getProductPrice() {
                return 5.0;
            }
        };
    }

    private CityReport getCityAReport() {
        return new CityReport() {
            @Override
            public Integer getSoldQuantity() {
                return 2;
            }

            @Override
            public String getCityName() {
                return "Beograd";
            }
        };
    }

    private CityReport getCityBReport() {
        return new CityReport() {
            @Override
            public Integer getSoldQuantity() {
                return 5;
            }

            @Override
            public String getCityName() {
                return "Novi Sad";
            }
        };
    }
}
package org.javapro.hw18.service;


import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Product;
import org.javapro.hw18.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class ProductServiceTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ProductService productService;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        product = Product.builder().id(1).name("cola").price(100).build();
        productDto = ProductDto.builder().id(1).name("cola").price(100).build();
    }

    @Test
    void getByIdTest() {
        int productId = 1;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.getById(productId);

        Mockito.verify(productRepository).findById(productId);
    }

    @Test
    void getByInvalidIdTest() {
        int invalidProductId = 999;

        Mockito.when(productRepository.findById(invalidProductId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.getById(invalidProductId)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void addProductTest() {
        Mockito.when(productRepository.save(product)).thenReturn(product);

        productService.addProduct(productDto);

        Mockito.verify(productRepository).save(product);
    }

    @Test
    void addInvalidAndNullProductTest() {
        ProductDto nullProductDto = null;
        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.addProduct(nullProductDto)
        );

        productDto.setName("");
        exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.addProduct(productDto)
        );
    }

    @Test
    void updateProductTest() {
        int productId = 2;

        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
        product.setId(productId);
        Mockito.when(productRepository.save(product)).thenReturn(product);


        productService.updateProduct(productId, productDto);

        Mockito.verify(productRepository).existsById(productId);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void updateInvalidProductTest() {
        int invalidProductId = 999;
        ProductDto productDto = new ProductDto();

        Mockito.when(productRepository.existsById(invalidProductId)).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.updateProduct(invalidProductId, productDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void changeProductTest() {
        int productId = 2;

        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
        product.setId(productId);
        Mockito.when(productRepository.save(product)).thenReturn(product);


        productService.changeProduct(productId, productDto);

        Mockito.verify(productRepository).existsById(productId);
        Mockito.verify(productRepository).save(product);

    }

    @Test
    void changeInvalidProductTest() {
        int invalidProductId = 999;
        ProductDto productDto = new ProductDto();

        Mockito.when(productRepository.existsById(invalidProductId)).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.changeProduct(invalidProductId, productDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void validateNewProductTest() {
        Assertions.assertDoesNotThrow(() -> productService.validateNewProduct(productDto));
    }

    @Test
    void validateInvalidAndNullNewProductTest() {
        ProductDto nullProductDto = null;
        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.validateNewProduct(nullProductDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        productDto.setName("");
        exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.validateNewProduct(productDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void validateExistingProductsTest() {
        ProductDto dto1 = productDto;
        ProductDto dto2 = ProductDto.builder().id(2).build();

        Mockito.when(productRepository.findAllById(List.of(1, 2)))
                .thenReturn(List.of(
                        product,
                        Product.builder().id(2).build()
                ));

        Assertions.assertDoesNotThrow(() ->
                productService.validateExistingProducts(List.of(dto1, dto2))
        );
    }

    @Test
    void validateExistingProductsWithInvalidProductTest() {
        ProductDto dto1 = productDto;
        ProductDto dto2 = ProductDto.builder().id(2).build();

        Mockito.when(productRepository.findAllById(List.of(1, 2)))
                .thenReturn(List.of(product));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> productService.validateExistingProducts(List.of(dto1, dto2))
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

}

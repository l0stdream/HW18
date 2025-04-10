package org.javapro.hw18.converter;

import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductConverterTest {
    @Test
    void toProductDtoTest(){
        Product product = Product.builder().id(1).name("cola").price(100).build();

        ProductDto productDto = ProductConverter.toProductDto(product);

        Assertions.assertEquals(product.getId(), productDto.getId());
        Assertions.assertEquals(product.getName(), productDto.getName());
        Assertions.assertEquals(product.getPrice(), productDto.getPrice());
    }

    @Test
    void toProductTest(){
        ProductDto productDto = ProductDto.builder().id(1).name("coal").price(100).build();

        Product product = ProductConverter.toProduct(productDto);

        Assertions.assertEquals(productDto.getId(), product.getId());
        Assertions.assertEquals(productDto.getName(), product.getName());
        Assertions.assertEquals(productDto.getPrice(), product.getPrice());
    }
}

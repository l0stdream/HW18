package org.javapro.hw18.converter;

import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Product;



public class ProductConverter {
    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public static Product toProduct(ProductDto productDto) {

        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .build();
    }
}

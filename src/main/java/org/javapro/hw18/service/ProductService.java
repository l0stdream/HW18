package org.javapro.hw18.service;

import lombok.AllArgsConstructor;
import org.javapro.hw18.converter.ProductConverter;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Product;
import org.javapro.hw18.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;


    public ProductDto getById(int id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Product doesn't exist."));
        return ProductConverter.toProductDto(product);
    }


    public void addProduct(ProductDto productDto) {
        validateNewProduct(productDto);
        Product product = ProductConverter.toProduct(productDto);
        productRepository.save(product);
    }

    public void updateProduct(int productId, ProductDto productDto) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product doesn't exist.");
        }
        validateNewProduct(productDto);
        productDto.setId(productId);
        addProduct(productDto);
    }

    public void changeProduct(int productId, ProductDto productDto){
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product doesn't exist.");
        }
        validateNewProduct(productDto);
        productDto.setId(productId);
        productRepository.save(ProductConverter.toProduct(productDto));
    }

    public void validateNewProduct(ProductDto productDto) {
        if (productDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product data is missing.");
        }

        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty.");
        }
    }

    public void validateExistingProducts(List<ProductDto> products) {
        List<Integer> existingProducts = productRepository.findAllById(products.stream()
                        .map(ProductDto::getId)
                        .toList()
                ).stream()
                .map(Product::getId)
                .toList();

        List<Integer> nonExistingProducts = products.stream()
                .map(ProductDto::getId)
                .filter(id -> !existingProducts.contains(id))
                .toList();

        if (!nonExistingProducts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Products with IDs: " + nonExistingProducts + " don't exist.");
        }
    }

}

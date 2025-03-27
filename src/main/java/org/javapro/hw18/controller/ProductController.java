package org.javapro.hw18.controller;

import lombok.AllArgsConstructor;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;


    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable int productId) {
        return this.productService.getById(productId);
    }

    @PostMapping
    public void addProduct(@RequestBody ProductDto productDto) {
        productService.addProduct(productDto);
    }
    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable int productId, @RequestBody ProductDto productDto){
        productService.updateProduct(productId, productDto);
    }

    @PatchMapping("/{productId}")
    public void changeProduct(@PathVariable int productId, @RequestBody ProductDto productDto){
        productService.changeProduct(productId, productDto);
    }

}

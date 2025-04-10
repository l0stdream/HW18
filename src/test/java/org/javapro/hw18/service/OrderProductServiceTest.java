package org.javapro.hw18.service;

import org.javapro.hw18.converter.ProductConverter;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.entity.Product;
import org.javapro.hw18.repository.OrderProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class OrderProductServiceTest {
    private final OrderProductRepository ordersProductsRepository = Mockito.mock(OrderProductRepository.class);
    private final ProductService productService = Mockito.mock(ProductService.class);
    private OrderProductService orderProductService;
    private Product product;
    private Order order;
    private OrderProductId orderProductId;
    private OrderProduct orderProduct;

    @BeforeEach
    void setUp() {
        orderProductService = new OrderProductService(ordersProductsRepository, productService);
        product = Product.builder().id(1).name("cola").price(100).build();
        order = Order.builder().id(10).comment("text").build();
        orderProductId = OrderProductId.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .build();
        int quantity = 2;
        orderProduct = OrderProduct.builder()
                .id(orderProductId)
                .order(order)
                .product(product)
                .quantity(quantity)
                .build();
    }

    @Test
    void getProductsByOrderIdTest() {
        Mockito.when(ordersProductsRepository.findOrderByOrderId(order.getId())).thenReturn(List.of(orderProduct));

        orderProductService.getProductsByOrderId(order.getId());

        Mockito.verify(ordersProductsRepository).findOrderByOrderId(order.getId());
    }

    @Test
    void getProductByOrderProductIdTest() {
        Mockito.when(ordersProductsRepository.findById(orderProductId)).thenReturn(Optional.of(orderProduct));
        orderProductService.getProductByOrderProductId(orderProductId);

        Mockito.verify(ordersProductsRepository).findById(orderProductId);
    }

    @Test
    void saveProductToOrderTest() {
        Mockito.when(ordersProductsRepository.save(orderProduct)).thenReturn(orderProduct);

        orderProductService.saveProductToOrder(orderProduct);

        Mockito.verify(ordersProductsRepository).save(orderProduct);
    }

    @Test
    void deleteAllProductsFromOrderByIdTest() {
        Mockito.when(ordersProductsRepository.deleteAllByOrderId(order.getId())).thenReturn(1);

        orderProductService.deleteAllProductsFromOrderById(order.getId());

        Mockito.verify(ordersProductsRepository).deleteAllByOrderId(order.getId());
    }

    @Test
    void deleteProductFromOrderByIdTest() {
        Mockito.doNothing().when(ordersProductsRepository).deleteById(orderProduct.getId());

        orderProductService.deleteProductFromOrderById(orderProduct);

        Mockito.verify(ordersProductsRepository).deleteById(orderProduct.getId());
    }

    @Test
    void updateProductInOrderByIdTest() {
        ProductDto productDto = ProductConverter.toProductDto(product);


        Mockito.doNothing().when(productService).validateExistingProducts(List.of(productDto));
        Mockito.when(ordersProductsRepository.findById(orderProductId)).thenReturn(Optional.of(orderProduct));
        Mockito.when(ordersProductsRepository.save(orderProduct)).thenReturn(orderProduct);

        orderProductService.updateProductInOrderById(orderProductId, productDto);

        Mockito.verify(productService).validateExistingProducts(List.of(productDto));
        Mockito.verify(ordersProductsRepository).findById(orderProductId);
        Mockito.verify(ordersProductsRepository).save(orderProduct);
    }

    @Test
    void updateInvalidProductInOrderByIdTest() {
        ProductDto productDto = ProductConverter.toProductDto(product);

        Mockito.when(ordersProductsRepository.findById(orderProductId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderProductService.updateProductInOrderById(orderProductId, productDto)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}

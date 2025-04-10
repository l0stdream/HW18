package org.javapro.hw18.service;

import org.javapro.hw18.dto.OrderDto;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;


public class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final ProductService productService = Mockito.mock(ProductService.class);
    private final OrderProductService orderProductService = Mockito.mock(OrderProductService.class);
    private OrderService orderService;


    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productService, orderProductService);
    }

    @Test
    void getValidOrderByIdTest() {
        int orderId = 1;
        Order order = Order.builder().id(orderId).build();

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getOrderById(orderId);
        Assertions.assertEquals(orderId, orderDto.getId());
    }

    @Test
    void getInvalidOrderByIdTest() {
        int invalidOrderId = 999;

        Mockito.when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.getOrderById(invalidOrderId)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void addOrderWithoutProductsTest() {
        orderService.addOrder(null);

        Mockito.verify(orderRepository).save(any(Order.class));
    }

    @Test
    void addValidOrderWithProductsTest() {
        OrderDto dto = OrderDto.builder()
                .products(List.of(new ProductDto()))
                .build();

        Order order = Order.builder().id(1).build();
        order.setId(1);

        Mockito.when(orderRepository.save(any())).thenReturn(order);

        OrderService spyService = Mockito.spy(new OrderService(orderRepository, productService, orderProductService));

        Mockito.doNothing().when(spyService).addProductsToOrder(eq(1), anyList());

        spyService.addOrder(dto);

        Mockito.verify(orderRepository).save(any(Order.class));
        Mockito.verify(spyService).addProductsToOrder(eq(1), anyList());
    }

    @Test
    void addProductsToOrderTest() {
        int orderId = 1;
        ProductDto productDto = ProductDto.builder()
                .id(3)
                .quantity(3)
                .build();

        List<ProductDto> products = List.of(productDto);

        Order order = Order.builder().id(orderId).build();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.doNothing().when(productService).validateExistingProducts(products);
        Mockito.doNothing().when(orderProductService).saveProductToOrder(any());

        orderService.addProductsToOrder(orderId, products);

        Mockito.verify(orderRepository).findById(orderId);
        Mockito.verify(productService).validateExistingProducts(products);
        Mockito.verify(orderProductService, Mockito.times(1)).saveProductToOrder(any());

    }

    @Test
    void addProductsToOrderWithNoProductsTest() {
        int orderId = 1;

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.addProductsToOrder(orderId, Collections.emptyList())
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void addProductsToInvalidOrderTest() {
        int invalidOrderId = 999;
        List<ProductDto> products = List.of(new ProductDto());

        Mockito.when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.addProductsToOrder(invalidOrderId, products)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void deleteOrderByOrderIdTest() {
        int orderId = 1;
        Order order = Order.builder().id(orderId).build();

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        Mockito.doNothing().when(orderRepository).delete(order);
        orderService.deleteOrderByOrderId(orderId);

        Mockito.verify(orderRepository).findById(orderId);
        Mockito.verify(orderRepository).delete(order);
    }

    @Test
    void deleteInvalidOrderByOrderIdTest() {
        int invalidOrderId = 999;

        Mockito.when(orderRepository.findById(invalidOrderId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.deleteOrderByOrderId(invalidOrderId)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void updateOrderByIdTest() {
        int orderId = 1;
        ProductDto productDto = ProductDto.builder()
                .id(3)
                .quantity(3)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(1)
                .products(List.of(productDto))
                .build();


        OrderService spyService = Mockito.spy(new OrderService(orderRepository, productService, orderProductService));

        Mockito.when(orderRepository.existsById(orderId)).thenReturn(true);
        Mockito.when(orderProductService.deleteAllProductsFromOrderById(orderId)).thenReturn(2);
        Mockito.doNothing().when(spyService).addProductsToOrder(orderId, orderDto.getProducts());

        Mockito.doNothing().when(orderRepository).updateById(orderId, orderDto.getComment());

        spyService.updateOrderById(orderId, orderDto);

        Mockito.verify(orderRepository).existsById(orderId);
        Mockito.verify(orderProductService).deleteAllProductsFromOrderById(orderId);
        Mockito.verify(spyService).addProductsToOrder(orderId, orderDto.getProducts());
        Mockito.verify(orderRepository).updateById(orderId, orderDto.getComment());
    }

    @Test
    void updateInvalidOrderByIdTest() {
        int invalidOrderId = 999;
        OrderDto orderDto = new OrderDto();

        Mockito.when(orderRepository.existsById(invalidOrderId)).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.updateOrderById(invalidOrderId, orderDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void deleteProductFromOrderTest() {
        int orderId = 1;
        int productId = 2;
        OrderProductId orderProductId = OrderProductId.builder()
                .orderId(orderId)
                .productId(productId)
                .build();

        OrderProduct orderProducts = OrderProduct.builder().id(orderProductId).build();


        Mockito.when(orderProductService.getProductByOrderProductId(orderProductId))
                .thenReturn(Optional.of(orderProducts));

        Mockito.doNothing().when(orderProductService).deleteProductFromOrderById(orderProducts);

        orderService.deleteProductFromOrder(orderId, productId);

        Mockito.verify(orderProductService).getProductByOrderProductId(orderProductId);
        Mockito.verify(orderProductService).deleteProductFromOrderById(orderProducts);
    }

    @Test
    void deleteInvalidProductFromOrderTest() {
        int orderId = 1;
        int invalidProductId = 999;
        OrderProductId orderProductId = new OrderProductId();

        Mockito.when(orderProductService.getProductByOrderProductId(orderProductId))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.deleteProductFromOrder(orderId, invalidProductId)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void changeProductInOrderByOrderIdTest() {
        int orderId = 1;
        int productId = 2;
        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .quantity(3)
                .build();

        OrderProductId orderProductId = OrderProductId.builder()
                .orderId(orderId)
                .productId(productId)
                .build();


        Mockito.when(orderRepository.existsById(orderId)).thenReturn(true);
        Mockito.doNothing().when(orderProductService).updateProductInOrderById(orderProductId, productDto);

        orderService.changeProductInOrderByOrderId(orderId, productId, productDto);

        Mockito.verify(orderRepository).existsById(orderId);
        Mockito.verify(orderProductService).updateProductInOrderById(orderProductId, productDto);
    }

    @Test
    void changeProductInOrderByInvalidOrderIdTest() {
        int invalidOrderId = 999;
        int productId = 2;
        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .quantity(3)
                .build();

        Mockito.when(orderRepository.existsById(invalidOrderId)).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.changeProductInOrderByOrderId(invalidOrderId, productId, productDto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void deleteAllProductsFromOrderByOrderIdTest() {
        int orderId = 1;

        Mockito.when(orderProductService.deleteAllProductsFromOrderById(orderId)).thenReturn(5);

        orderService.deleteAllProductsFromOrderByOrderId(orderId);

        Mockito.verify(orderProductService).deleteAllProductsFromOrderById(orderId);
    }

    @Test
    void deleteAllProductsFromOrderByInvalidOrderIdTest() {
        int invalidOrderId = 999;

        Mockito.when(orderProductService.deleteAllProductsFromOrderById(invalidOrderId)).thenReturn(0);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> orderService.deleteAllProductsFromOrderByOrderId(invalidOrderId)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}

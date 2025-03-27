package org.javapro.hw18.controller;

import lombok.AllArgsConstructor;
import org.javapro.hw18.dto.OrderDto;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;


    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping
    public void addOrder(@RequestBody(required = false) OrderDto orderDto) {
        orderService.addOrder(orderDto);
    }


    @PostMapping("/{orderId}")
    public void addProductToOrder(@PathVariable int orderId, @RequestBody List<ProductDto> products) {
        orderService.addProductsToOrder(orderId, products);
    }

    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable int orderId, @RequestBody OrderDto orderDto) {
        orderService.updateOrderById(orderId, orderDto);
    }

    @PatchMapping("/{orderId}/products/{productId}")
    public void changeProductInOrderByOrderId(@PathVariable int orderId, @PathVariable int productId, @RequestBody ProductDto productDto) {
        orderService.changeProductInOrderByOrderId(orderId, productId, productDto);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrderByOrderId(@PathVariable int orderId) {
        orderService.deleteOrderByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}/products")
    public void deleteAllProductInOrderByOrderId(@PathVariable int orderId) {
        orderService.deleteAllProductsFromOrderByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public void deleteProductFromOrder(@PathVariable int orderId, @PathVariable int productId) {
        orderService.deleteProductFromOrder(orderId, productId);
    }
}


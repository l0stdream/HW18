package org.javapro.hw18.controller;

import org.javapro.hw18.dto.Order;
import org.javapro.hw18.dto.Product;
import org.javapro.hw18.service.OrderService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public void addOrder(@RequestBody Order order) {
            this.orderService.addOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable int id) {
        return this.orderService.getOrder(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable int id) {
        this.orderService.deleteOrder(id);
    }

    @PutMapping("/{id}")
    public void updateOrder(@PathVariable int id, @RequestBody Order order) {
            this.orderService.updateOrder(id, order);
    }

    @PatchMapping("/{id}/products")
    public void addProductToOrder(@PathVariable int id, @RequestBody Product product) {
        this.orderService.addProductToOrder(id, product);
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public void deleteProductFromOrder(@PathVariable int orderId, @PathVariable int productId) {
        this.orderService.deleteProductFromOrder(orderId, productId);
    }
}

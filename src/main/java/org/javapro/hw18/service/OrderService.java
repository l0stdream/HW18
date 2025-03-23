package org.javapro.hw18.service;

import org.javapro.hw18.dto.Order;
import org.javapro.hw18.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final Map<Integer, Order> orders;

    @Autowired
    public OrderService(Map<Integer, Order> orders) {
        this.orders = orders;
    }


    public void addOrder(Order order) {
        if (orders.get(order.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order with that ID already exists");
        } else {
            orders.put(order.getId(), order);
        }
    }


    public Order getOrder(int id) {
        if (orders.get(id) != null) {
            return orders.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order doesn't exist.");
        }
    }


    public void deleteOrder(int id) {
        if (orders.get(id) != null) {
            orders.remove(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order doesn't exist.");
        }
    }


    public void updateOrder(int id, Order order) {
        if (order.getId() != id) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cant change your order ID");
        } else {
            orders.put(id, order);
        }
    }

    public void addProductToOrder(int orderId, Product product) {
        Order order = orders.get(orderId);
        if (order != null) {
            List<Product> products = order.getProducts();
            if (products == null) {
                products = new ArrayList<>();
            }
            products.add(product);
            order.setProducts(products);
            orders.put(orderId, order);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order doesn't exist.");
        }
    }

    public void deleteProductFromOrder(int orderId, int productId) {
        Order order = orders.get(orderId);

        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order doesn't exist.");
        }

        List<Product> products = order.getProducts();

        if (products == null || products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product doesn't exist in this order.");
        }

        boolean productRemoved = products.removeIf(product -> product.getId() == productId);

        if (!productRemoved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product doesn't exist in this order.");
        }

        order.setProducts(products);
        orders.put(orderId, order);
    }
}

package org.javapro.hw18.converter;

import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.entity.Product;

public class OrderProductConverter {

    public static OrderProduct toOrderProduct(Order order, Product product, int quantity) {
        OrderProduct orderProduct = new OrderProduct();
        OrderProductId id = new OrderProductId();
        id.setOrderId(order.getId());
        id.setProductId(product.getId());
        orderProduct.setId(id);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(quantity);
        return orderProduct;
    }
}

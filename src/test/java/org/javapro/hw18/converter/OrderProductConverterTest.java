package org.javapro.hw18.converter;

import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.javapro.hw18.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderProductConverterTest {

    @Test
    void toOrderProductTest(){
        Product product = Product.builder().id(1).name("cola").price(100).build();
        Order order = Order.builder().id(10).comment("text").build();
        int quantity = 2;
        OrderProductId orderProductId = OrderProductId.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .build();

        OrderProduct orderProduct = OrderProductConverter.toOrderProduct(order,product,quantity);

        Assertions.assertEquals(order, orderProduct.getOrder());
        Assertions.assertEquals(product, orderProduct.getProduct());
        Assertions.assertEquals(quantity, orderProduct.getQuantity());
        Assertions.assertEquals(orderProductId, orderProduct.getId());
    }
}

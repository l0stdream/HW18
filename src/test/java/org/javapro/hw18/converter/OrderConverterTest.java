package org.javapro.hw18.converter;

import org.javapro.hw18.converter.OrderConverter;
import org.javapro.hw18.dto.OrderDto;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OrderConverterTest {

    @Test
    void toOrderDtoTest() {
        Product product = Product.builder().id(1).name("cola").price(100).build();
        Order order = Order.builder().id(10).comment("text").build();
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .quantity(2)
                .build();

        OrderDto orderDto = OrderConverter.toOrderDto(order, List.of(orderProduct));

        Assertions.assertEquals(order.getId(), orderDto.getId());
        Assertions.assertEquals(order.getComment(), orderDto.getComment());
        Assertions.assertEquals(1, orderDto.getProducts().size());
        Assertions.assertEquals(product.getId(), orderDto.getProducts().get(0).getId());
        Assertions.assertEquals(2, orderDto.getProducts().get(0).getQuantity());
    }

    @Test
    void toOrderTest() {
        OrderDto orderDto = OrderDto.builder().id(1).comment("text")
                .products(List.of(new ProductDto())).build();

        Order order = OrderConverter.toOrder(orderDto);

        Assertions.assertEquals(orderDto.getId(), order.getId());
        Assertions.assertEquals(orderDto.getComment(), order.getComment());
    }
}

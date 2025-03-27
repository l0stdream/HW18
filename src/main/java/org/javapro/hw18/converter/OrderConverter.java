package org.javapro.hw18.converter;

import org.aspectj.weaver.ast.Or;
import org.javapro.hw18.dto.OrderDto;
import org.javapro.hw18.dto.ProductDto;
import org.javapro.hw18.entity.Order;
import org.javapro.hw18.entity.OrderProduct;

import java.util.List;

public class OrderConverter {
    public static OrderDto toOrderDto(Order order, List<OrderProduct> orderProducts) {
        List<ProductDto> products = orderProducts.stream()
                .map(op -> ProductDto.builder()
                        .id(op.getProduct().getId())
                        .name(op.getProduct().getName())
                        .price(op.getProduct().getPrice())
                        .quantity(op.getQuantity())
                        .build())
                .toList();

        return OrderDto.builder()
                .id(order.getId())
                .comment(order.getComment())
                .products(products)
                .build();
    }

    public static Order toOrder(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .comment(orderDto.getComment())
                .build();
    }
}

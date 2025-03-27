package org.javapro.hw18.entity;

import jakarta.persistence.*;

import lombok.*;

@Data
@Entity
@Table(name = "orders_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private int quantity;
}

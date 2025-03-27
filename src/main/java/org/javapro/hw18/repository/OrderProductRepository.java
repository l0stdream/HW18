package org.javapro.hw18.repository;

import org.javapro.hw18.entity.OrderProduct;
import org.javapro.hw18.entity.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.product WHERE op.id.orderId = :orderId")
    List<OrderProduct> findOrderByOrderId(int orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderProduct op WHERE op.id.orderId = :orderId")
    int deleteAllByOrderId(int orderId);

}

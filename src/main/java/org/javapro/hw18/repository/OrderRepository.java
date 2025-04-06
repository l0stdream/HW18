package org.javapro.hw18.repository;


import org.javapro.hw18.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("UPDATE Order o SET o.comment = :comment WHERE o.id = :id")
    @Modifying
    void updateById(@Param("id") int id, @Param("comment") String comment);

}

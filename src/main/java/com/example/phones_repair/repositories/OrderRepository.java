package com.example.phones_repair.repositories;

import com.example.phones_repair.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status = :status")
    List<Order> findByStatus(@Param("status") String status);

    @Query("select o from Order o where o.client.id = :clientId")
    List<Order> findByClientId(@Param("clientId") Long clientId);


    @Query("select o from Order o where o.order_name like concat(:orderOption, '_%')")
    List<Order> findByOrderOption(@Param("orderOption") String orderOption);

    @Query("select o.id as id, o.client.id as clientId, o.order_name as orderName," +
            " o.status as status, o.price as price " +
            "from Order o where o.price = (select MAX(o2.price) from Order o2)")
    List<Map<String, Object>> findLargestOrders();

    @Query("select o.id as id, o.client.id as clientId, o.order_name as orderName," +
            " o.status as status, o.price as price " +
            "from Order o where o.price = (select MIN(o2.price) from Order o2)")
    List<Map<String, Object>> findSmallestOrders();

}

package com.project.webpos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.OrderList;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Integer>{
  List<OrderList> findByFoodId(Long foodId);
  List<OrderList> findByTable_Id(Integer foodId);
  List<OrderList> findByReceiptNo(String receiptNo);
  OrderList findByReceiptNoAndFoodId(String receiptNo, Long foodId);
  
  @Query("SELECT ol " +
         "FROM OrderList ol " +
         "WHERE ol.foodId =  :foodId AND ol.placed = false"
  )
  List<OrderList> findByFoodIdAndUnplaced(@Param("foodId") Long foodId);

  @Query("SELECT ol " +
         "FROM OrderList ol " +
         "ORDER BY ol.receiptNo DESC " + 
         "LIMIT 1")
  OrderList findCurrentReceiptNo();

  @Query("SELECT ol " +
        "FROM OrderList ol " +
        "WHERE ol.placed = false"
  )
  List<OrderList> findUnplacedOrders();
  
  @Query("SELECT ol " +
        "FROM OrderList ol " +
        "WHERE ol.placed = false " +
        "LIMIT 1"
  )
  OrderList findUnplacedOrder();
}

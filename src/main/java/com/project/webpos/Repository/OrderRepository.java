package com.project.webpos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.OrderList;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Integer>{
  List<OrderList> findByNumber(Long number);
  List<OrderList> findByTable_Id(Integer number);
  List<OrderList> findByReceiptNo(String receiptNo);
  OrderList findByReceiptNoAndNumber(String receiptNo, Long number);
  OrderList findByNumberAndTableId(Long number, Integer tableId);
  OrderList findByNumberAndTableIdAndSizeId(Long number, Integer tableId, Integer sizeId);
  
  @Query("SELECT ol " +
         "FROM OrderList ol " +
         "WHERE ol.number =  :number AND ol.placed = false"
  )
  List<OrderList> findByNumberAndUnplaced(@Param("number") Long number);

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

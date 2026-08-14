package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.OrderList;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Integer>{
  OrderList findByFoodId(Long foodId);
}

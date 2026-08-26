package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.OrderType;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {

}

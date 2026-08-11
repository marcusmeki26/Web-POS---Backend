package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

}

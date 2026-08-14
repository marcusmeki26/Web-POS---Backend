package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.TableList;

@Repository
public interface TableRepository extends JpaRepository<TableList, Integer> {

}

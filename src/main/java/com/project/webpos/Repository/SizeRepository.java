package com.project.webpos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.webpos.Entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

}

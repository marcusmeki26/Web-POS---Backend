package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.TableList;
import com.project.webpos.Repository.TableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableService {
  private final TableRepository tableRepository;

  public TableList getTableById(Integer id){
    return tableRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("No existing table id"));
  }
}

package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.TableList;
import com.project.webpos.Entity.DTO.TableListResponse;
import com.project.webpos.Mapper.TableListMapper;
import com.project.webpos.Repository.TableListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableListService {
  private final TableListRepository tableRepository;
  private final TableListMapper tableListMapper;

  public TableList getTableById(Integer id){
    return tableRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("No existing table id"));
  }

  public List<TableListResponse> getTables(){
    return tableListMapper.toResponseList(tableRepository.findAll());
  }
}

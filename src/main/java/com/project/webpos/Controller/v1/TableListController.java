package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.webpos.Entity.DTO.TableListResponse;
import com.project.webpos.Service.TableListService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/table")
public class TableListController {
  private final TableListService tableListService;

  @GetMapping
  public List<TableListResponse> getTables(){
    return tableListService.getTables();
  }
}

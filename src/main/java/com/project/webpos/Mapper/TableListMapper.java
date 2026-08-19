package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.project.webpos.Entity.TableList;
import com.project.webpos.Entity.DTO.TableListResponse;

@Mapper(componentModel = "spring")
public interface TableListMapper {
  TableListResponse toResponse(TableList tableList);

  List<TableListResponse> toResponseList(List<TableList> tableList);
}

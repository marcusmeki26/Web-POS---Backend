package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.SizeResponse;

@Mapper (componentModel = "spring")
public interface SizeMapper {
  SizeResponse toResponse(Size size);

  List<SizeResponse> toResponseList(List<Size> sizes);
}

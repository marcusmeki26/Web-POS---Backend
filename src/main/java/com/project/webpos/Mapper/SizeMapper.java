package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

import com.project.webpos.Entity.Size;
import com.project.webpos.Entity.DTO.SizeCreateResponse;
import com.project.webpos.Entity.DTO.SizePatchRequest;
import com.project.webpos.Entity.DTO.SizePatchResponse;
import com.project.webpos.Entity.DTO.SizeResponse;

@Mapper (
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SizeMapper {
  SizeResponse toResponse(Size size);
  List<SizeResponse> toResponseList(List<Size> sizes);

  SizePatchResponse toPatchResponse(Size size);

  SizeCreateResponse toCreateResponse(Size size);

  void patchSize(SizePatchRequest patchRequest, @MappingTarget Size size);
  // This generic fallback unwraps ALL JsonNullable fields automatically
  default <T> T unwrap(JsonNullable<T> jsonNullable) {
      return jsonNullable == null ? null : jsonNullable.orElse(null); 
  }

  // This tells MapStruct to ONLY touch fields that the user intentionally provided
  @Condition
  default <T> boolean isPresent(JsonNullable<T> jsonNullable) {
      return jsonNullable != null && jsonNullable.isPresent();
  }
}

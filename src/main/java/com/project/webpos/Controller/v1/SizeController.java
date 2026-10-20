package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Service.SizeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.project.webpos.Entity.DTO.SizeResponse;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/v1/size")
@PreAuthorize("hasRole('OWNER')")
@Tag(name = "Size", description = "Endpoints for manageing Size")
public class SizeController {
  private final SizeService sizeService;

  // @GetMapping
  // public ResponseEntity<List<SizeResponse>> getSizeByFoodId(
  //   @RequestParam(name = "food", required = false) Long foodId
  // ){
  //   return ResponseEntity.ok(sizeService.getSizeByFoodId(foodId));
  // }

  @GetMapping 
  @Operation(
    summary = "Retrieves all the sizes",
    description = "Fetches all the sizes"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public ResponseEntity<List<SizeResponse>> getSizes(){
    return ResponseEntity.ok(sizeService.getSizes());
  }
}

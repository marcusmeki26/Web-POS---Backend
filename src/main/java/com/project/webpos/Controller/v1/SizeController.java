package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Service.SizeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.project.webpos.Entity.DTO.SizeResponse;
import com.project.webpos.Exception.ErrorMessage;
import com.project.webpos.Entity.DTO.SizeCreateRequest;
import com.project.webpos.Entity.DTO.SizeCreateResponse;
import com.project.webpos.Entity.DTO.SizePatchRequest;
import com.project.webpos.Entity.DTO.SizePatchResponse;

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

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
    summary = "Creates a new size",
    description = "Adds the new size. Returns the size id & size name"
  )
  @ApiResponse(responseCode = "201", description = "Successfully added")
  public SizeCreateResponse postSize(
    @RequestBody SizeCreateRequest postRequest
  ){
    return sizeService.postSize(postRequest);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Partially modifies a size",
    description = "Updates only the specified field of the targer size. Omitted values are remain untouched"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated"),
    @ApiResponse(responseCode = "404", description = "Size not found")
  })
  public SizePatchResponse patchSize(
    @Parameter(description = "ID of the target size to modify", required = true, example = "1")
    @PathVariable Integer id,
    @RequestBody SizePatchRequest patchRequest
  ){
    return sizeService.patchSize(patchRequest, id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Hard deletes a size",
    description = "Hard deletes a size based on the given size ID"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
    @ApiResponse(responseCode = "409", description = "Size is active reference on an active food. Unable to delete", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public void deleteSize(
    @PathVariable Integer id
  ){
    sizeService.deleteSize(id);
  }
}

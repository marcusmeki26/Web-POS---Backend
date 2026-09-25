package com.project.webpos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Size;
import com.project.webpos.Repository.SizeRepository;
import com.project.webpos.Entity.DTO.SizeResponse;
import com.project.webpos.Mapper.SizeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeService {
  private final SizeRepository sizeRepository;
  private final SizeMapper sizeMapper;

  public Size getSize(Integer id){
    return sizeRepository.findById(id)
            .orElseThrow();
  }

  public List<SizeResponse> getSizes(){
    return sizeMapper.toResponseList(sizeRepository.findAll());
  }

  // public List<SizeResponse> getSizeByFoodId(Long foodId){
  //   return List.of(new SizeResponse());
  // }
}

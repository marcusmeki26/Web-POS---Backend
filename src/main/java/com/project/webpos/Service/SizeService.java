package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Entity.Size;
import com.project.webpos.Repository.SizeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeService {
  private final SizeRepository sizeRepository;

  public Size getSize(Integer id){
    return sizeRepository.findById(id)
            .orElseThrow();
  }
}

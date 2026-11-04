package com.project.webpos.Entity.DTO;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class PagedResponse<T> implements Serializable {
  private List<T> content;
  private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public PagedResponse(Page<T> page) {
      this.content = page.getContent();
      this.pageNumber = page.getNumber();
      this.pageSize = page.getSize();
      this.totalElements = page.getTotalElements();
      this.totalPages = page.getTotalPages();
      this.isLast = page.isLast();
  }
}

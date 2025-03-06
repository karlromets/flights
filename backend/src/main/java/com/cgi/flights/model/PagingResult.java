package com.cgi.flights.model;

import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;

// https://ardijorganxhi.medium.com/implement-pagination-at-your-spring-boot-application-a540270b5f60

@Data
@NoArgsConstructor
public class PagingResult<T> {

  private Collection<T> content;
  private Integer totalPages;
  private long totalElements;
  private Integer size;
  private Integer page;
  private boolean empty;

  public PagingResult(
      Collection<T> content,
      Integer totalPages,
      long totalElements,
      Integer size,
      Integer page,
      boolean empty) {
    this.content = content;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.size = size;
    this.page = page + 1;
    this.empty = empty;
  }
}

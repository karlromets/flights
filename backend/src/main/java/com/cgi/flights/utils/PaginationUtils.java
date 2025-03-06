package com.cgi.flights.utils;

import com.cgi.flights.model.PaginationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

// https://ardijorganxhi.medium.com/implement-pagination-at-your-spring-boot-application-a540270b5f60

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {

  public static Pageable getPageable(PaginationRequest request) {
    return PageRequest.of(
        request.getPage(), request.getSize(), request.getDirection(), request.getSortField());
  }
}

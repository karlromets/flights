package com.cgi.flights.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

// https://ardijorganxhi.medium.com/implement-pagination-at-your-spring-boot-application-a540270b5f60

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

  @Builder.Default private Integer page = 1;

  @Builder.Default private Integer size = 10;

  @Builder.Default private String sortField = "id";

  @Builder.Default private Sort.Direction direction = Sort.Direction.DESC;
}

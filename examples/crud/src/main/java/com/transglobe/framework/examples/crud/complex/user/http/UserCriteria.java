package com.transglobe.framework.examples.crud.complex.user.http;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Data
public class UserCriteria {

  String name;
  LocalDate birthdayStart;
  LocalDate birthdayEnd;

  Pageable pageable;
}

package com.transglobe.framework.examples.crud.complex.user.http;

import java.time.LocalDate;
import lombok.Data;

@Data
class ReplaceUserDto {

  String name;
  LocalDate birthday;
}

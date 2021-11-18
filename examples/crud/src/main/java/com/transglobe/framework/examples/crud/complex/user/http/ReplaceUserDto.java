package com.transglobe.framework.examples.crud.complex.user.http;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ReplaceUserDto {

  String name;
  LocalDate birthday;
}

package com.transglobe.framework.examples.crud.complex.user.http;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.Data;

@Data
class CreateUserDto {

  @NotBlank
  String name;

  @Past
  LocalDate birthday;

  @NotBlank
  String password;
}

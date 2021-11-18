package com.transglobe.framework.examples.crud.complex.user.domain;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.Data;

@Data
public class CreateUser {

  @NotBlank
  String name;

  @Past
  LocalDate birthday;

  @NotBlank
  String password;
}

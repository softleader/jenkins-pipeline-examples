package com.transglobe.framework.examples.crud.complex.user.domain;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ReplaceUser {

  @PositiveOrZero
  Long id;

  @NotBlank
  String name;

  @Past
  LocalDate birthday;
}

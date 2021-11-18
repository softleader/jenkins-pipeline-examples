package com.transglobe.framework.examples.crud.complex.user.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ChangePassword {

  @PositiveOrZero
  long id;

  @NotBlank
  String password;
}

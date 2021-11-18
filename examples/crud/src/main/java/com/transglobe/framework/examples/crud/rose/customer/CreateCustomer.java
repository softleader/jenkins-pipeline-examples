package com.transglobe.framework.examples.crud.rose.customer;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.Data;

@Data
public class CreateCustomer {

  @NotBlank
  String name;

  @Past
  LocalDate birthday;

  @NotBlank
  String password;
}

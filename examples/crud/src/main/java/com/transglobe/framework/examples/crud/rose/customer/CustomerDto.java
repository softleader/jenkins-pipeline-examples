package com.transglobe.framework.examples.crud.rose.customer;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDto {
  Long id;
  String name;
  LocalDate birthday;
  String password;
}

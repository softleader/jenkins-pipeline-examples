package com.transglobe.framework.examples.crud.rose.customer;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReplaceCustomer {

  String name;
  LocalDate birthday;
}

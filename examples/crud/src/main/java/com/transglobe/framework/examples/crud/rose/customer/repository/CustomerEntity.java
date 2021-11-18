package com.transglobe.framework.examples.crud.rose.customer.repository;

import com.transglobe.framework.data.jpa.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue
  Long id;

  @NotBlank
  String name;

  @Past
  LocalDate birthday;

  @NotBlank
  String password;
}

package com.transglobe.framework.examples.crud.complex.user.repository;

import com.transglobe.framework.data.jpa.AbstractAuditingEntity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class UserEntity extends AbstractAuditingEntity<Long> {

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

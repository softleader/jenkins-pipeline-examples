package com.transglobe.framework.examples.crud.rose.customer;

import com.transglobe.framework.examples.crud.PasswordInvalidException;
import com.transglobe.framework.examples.crud.rose.customer.http.CustomerCriteria;
import com.transglobe.framework.examples.crud.rose.customer.mapper.CustomerMapper;
import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerEntity;
import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerRepository;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
@RequiredArgsConstructor
public class CustomerService {

  final CustomerRepository repository;
  final CustomerMapper mapper;

  public Page<CustomerEntity> getAll(CustomerCriteria criteria, Pageable pageable) {
    return repository.findAll(pageable);
  }

  public long createCustomer(@Validated @NonNull CreateCustomer user) {
    checkPassword(user.getPassword());
    return repository.save(mapper.toEntity(user)).getId();
  }

  public void replaceCustomer(long id, @Validated @NonNull ReplaceCustomer user) {
    repository.findById(id)
        .map(db -> mapper.replace(user, db))
        .map(repository::save)
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("ID [%s] not exist", id)));
  }

  public void changePassword(long id, @Validated @NonNull ChangePassword cmd) {
    checkPassword(cmd.getPassword());
    repository.findById(id)
        .map(db -> mapper.replace(cmd, db))
        .map(repository::save)
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("ID [%s] not exist", id)));
  }

  private void checkPassword(@NonNull String password) {
    if (password.length() < 5) {
      throw new PasswordInvalidException("Password must contains at least 10 characters");
    }
    if (!password.matches("(?=.*\\d).*")) {
      throw new PasswordInvalidException("Password must contains at least 1 digit");
    }
    if (!password.matches("(?=.*[a-z])(?=.*[A-Z]).*")) {
      throw new PasswordInvalidException(
          "Password must contains at least 1 upper and 1 lower case character");
    }
  }

  public void deleteCustomers(@NonNull Collection<Long> ids) {
    repository.deleteAllById(ids);
  }
}

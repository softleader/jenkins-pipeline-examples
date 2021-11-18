package com.transglobe.framework.examples.crud.traditional.client.service;

import com.transglobe.framework.examples.crud.PasswordInvalidException;
import com.transglobe.framework.examples.crud.traditional.client.http.ClientCriteria;
import com.transglobe.framework.examples.crud.traditional.client.repository.ClientEntity;
import com.transglobe.framework.examples.crud.traditional.client.repository.ClientRepository;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
@RequiredArgsConstructor
public class ClientService {

  final ClientRepository repository;

  public Page<ClientEntity> getAll(ClientCriteria criteria, Pageable pageable) {
    return repository.findAll(pageable);
  }

  public long createClient(@Validated @NonNull ClientEntity user) {
    checkPassword(user.getPassword());
    return repository.save(user).getId();
  }

  public void replaceClient(@Validated @NonNull ClientEntity user) {
    repository.findById(user.getId())
        .map(db -> {
          db.setName(user.getName());
          db.setBirthday(user.getBirthday());
          if (StringUtils.hasText(user.getPassword())) {
            checkPassword(user.getPassword());
            db.setPassword(user.getPassword());
          }
          return db;
        })
        .map(repository::save)
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("ID [%s] not exist", user.getId())));
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

  public void deleteClients(@NonNull Collection<Long> ids) {
    repository.deleteAllById(ids);
  }
}

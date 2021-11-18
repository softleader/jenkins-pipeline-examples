package com.transglobe.framework.examples.crud.complex.user.application;

import com.transglobe.framework.examples.crud.complex.user.domain.ChangePassword;
import com.transglobe.framework.examples.crud.complex.user.domain.CreateUser;
import com.transglobe.framework.examples.crud.complex.user.domain.ReplaceUser;
import com.transglobe.framework.examples.crud.complex.user.domain.User;
import com.transglobe.framework.examples.crud.complex.user.http.UserCriteria;
import com.transglobe.framework.examples.crud.complex.user.repository.UserEntityMapper;
import com.transglobe.framework.examples.crud.complex.user.repository.UserRepository;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
@RequiredArgsConstructor
public class UserService {

  final UserRepository repository;
  final UserEntityMapper mapper;

  public Page<User> getAll(UserCriteria criteria) {
    return repository.findAll(criteria.getPageable()).map(mapper::fromEntity);
  }

  public long createUser(@Validated @NonNull CreateUser user) {
    return repository.save(mapper.toEntity(User.createUser(user))).getId();
  }

  public void replaceUser(@Validated @NonNull ReplaceUser user) {
    repository.findById(user.getId())
        .map(db -> mapper.replace(user, db))
        .map(repository::save)
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("ID [%s] not exist", user.getId())));
  }

  public void changePassword(@Validated @NonNull ChangePassword cmd) {
    repository.findById(cmd.getId())
        .map(db -> {
          var user = mapper.fromEntity(db);
          user.changePassword(cmd.getPassword());
          return mapper.replace(user, db);
        })
        .map(repository::save)
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("ID [%s] not exist", cmd.getId())));
  }

  public void deleteUsers(@NonNull Collection<Long> ids) {
    repository.deleteAllById(ids);
  }
}

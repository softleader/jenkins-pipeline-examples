package com.transglobe.framework.examples.crud.complex.user.repository;

import com.transglobe.framework.examples.crud.complex.user.domain.ReplaceUser;
import com.transglobe.framework.examples.crud.complex.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserEntityMapper {

  UserEntity toEntity(User user);

  UserEntity replace(ReplaceUser user, @MappingTarget UserEntity entity);

  UserEntity replace(User user, @MappingTarget UserEntity entity);

  User fromEntity(UserEntity entity);
}

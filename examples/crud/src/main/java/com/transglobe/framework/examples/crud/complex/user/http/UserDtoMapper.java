package com.transglobe.framework.examples.crud.complex.user.http;

import com.transglobe.framework.examples.crud.complex.user.domain.ChangePassword;
import com.transglobe.framework.examples.crud.complex.user.domain.CreateUser;
import com.transglobe.framework.examples.crud.complex.user.domain.User;
import com.transglobe.framework.examples.crud.complex.user.repository.UserEntity;
import com.transglobe.framework.examples.crud.complex.user.domain.ReplaceUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserDtoMapper {

  UserDto toDto(User user);

  CreateUser fromDto(CreateUserDto dto);

  @Mapping(source = "id", target = "id")
  ReplaceUser fromDto(Long id, ReplaceUserDto dto);

  @Mapping(source = "id", target = "id")
  ChangePassword fromDto(Long id, ChangePasswordDto dto);
}

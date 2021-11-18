package com.transglobe.framework.examples.crud.complex.user.http;

import com.transglobe.framework.examples.crud.complex.user.application.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Complex Style")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

  final UserService service;
  final UserDtoMapper mapper;

  @GetMapping
  Page<UserDto> listUsers(UserCriteria criteria) {
    return service.getAll(criteria).map(mapper::toDto);
  }

  @PostMapping
  Long createUser(@Validated @RequestBody CreateUserDto dto) {
    var command = mapper.fromDto(dto);
    return service.createUser(command);
  }

  @PutMapping("/{id}")
  void replaceUser(@PathVariable("id") long id,
      @Validated @RequestBody ReplaceUserDto dto) {
    service.replaceUser(mapper.fromDto(id, dto));
  }

  @PatchMapping("/{id}/change-password")
  void changePassword(@PathVariable("id") long id,
      @Validated @RequestBody ChangePasswordDto dto) {
    service.changePassword(mapper.fromDto(id, dto));
  }

  @DeleteMapping("/{ids}")
  void deleteUsers(@PathVariable("ids") Collection<Long> ids) {
    service.deleteUsers(ids);
  }

}

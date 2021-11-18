package com.transglobe.framework.examples.crud.complex.user.domain;

import com.transglobe.framework.examples.crud.PasswordInvalidException;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @NotBlank
  String name;

  @NotNull
  @Past
  LocalDate birthday;

  @NotBlank
  String password;

  public static User createUser(CreateUser command) {
    var user = User.builder()
        .name(command.getName())
        .birthday(command.getBirthday())
        .password(command.getPassword())
        .build();
    user.checkPassword();
    return user;
  }

  public User changePassword(@NonNull String newPassword) {
    this.password = newPassword;
    checkPassword();
    return this;
  }

  void checkPassword() {
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
}

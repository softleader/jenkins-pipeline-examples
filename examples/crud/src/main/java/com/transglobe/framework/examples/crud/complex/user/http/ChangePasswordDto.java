package com.transglobe.framework.examples.crud.complex.user.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ChangePasswordDto {

  String password;
}

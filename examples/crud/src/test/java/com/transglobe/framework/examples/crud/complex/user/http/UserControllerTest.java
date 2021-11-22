package com.transglobe.framework.examples.crud.complex.user.http;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transglobe.framework.examples.crud.complex.user.repository.UserRepository;
import com.transglobe.framework.test.TransGlobeTest;
import com.transglobe.framework.test.autoconfigure.redis.AutoConfigureRedisContainer;
import java.time.LocalDate;
import java.util.Objects;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRedisContainer
@Timeout(1)
@TransGlobeTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  UserRepository userRepository;

  @Test
  @SneakyThrows
  void testCRUD() {
    var created = CreateUserDto.builder()
        .birthday(LocalDate.of(1990, 1, 1))
        .name("hello")
        .password("helloHELLO123")
        .build();
    var id = mockMvc
        .perform(
            post("/users")
                .content(mapper.writeValueAsString(created))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(isNumeric()))
        .andDo(print())
        .andReturn().getResponse().getContentAsString();

    var replaced = ReplaceUserDto.builder()
        .name("world")
        .birthday(LocalDate.of(1990, 2, 2))
        .build();
    mockMvc
        .perform(
            put("/users/{id}", id)
                .content(mapper.writeValueAsString(replaced))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
    assertThat(userRepository.findById(parseLong(id)))
        .isPresent()
        .get()
        .extracting("name", "birthday", "password")
        .contains(replaced.getName(), replaced.getBirthday(), created.getPassword());

    var changed = ChangePasswordDto.builder()
        .password("worldWORLD123")
        .build();
    mockMvc
        .perform(
            patch("/users/{id}/change-password", id)
                .content(mapper.writeValueAsString(changed))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
    assertThat(userRepository.findById(parseLong(id)))
        .isPresent()
        .get()
        .extracting("password")
        .isEqualTo(changed.getPassword());

    mockMvc
        .perform(
            delete("/users/{id}", id)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
    assertThat(userRepository.findById(parseLong(id)))
        .isEmpty();
  }

  private Matcher<String> isNumeric() {
    return new CustomMatcher<>("a numeric string") {

      @Override
      public boolean matches(Object actual) {
        return StringUtils.isNumeric(Objects.toString(actual));
      }
    };
  }
}

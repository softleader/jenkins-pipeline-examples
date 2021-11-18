package com.transglobe.framework.examples.crud.rose.customer.http;

import com.transglobe.framework.examples.crud.rose.customer.ChangePassword;
import com.transglobe.framework.examples.crud.rose.customer.CreateCustomer;
import com.transglobe.framework.examples.crud.rose.customer.CustomerService;
import com.transglobe.framework.examples.crud.rose.customer.ReplaceCustomer;
import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Tag(name = "Rose Style")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
class CustomerController {

  final CustomerService service;

  @GetMapping
  Page<CustomerEntity> listCustomers(CustomerCriteria criteria, Pageable pageable) {
    return service.getAll(criteria, pageable);
  }

  @PostMapping
  Long createCustomer(@Validated @RequestBody CreateCustomer user) {
    return service.createCustomer(user);
  }

  @PutMapping("/{id}")
  void replaceCustomer(@PathVariable("id") long id, @Validated @RequestBody ReplaceCustomer user) {
    service.replaceCustomer(id, user);
  }

  @PatchMapping("/{id}/change-password")
  void changePassword(@PathVariable("id") long id, @Validated @RequestBody ChangePassword user) {
    service.changePassword(id, user);
  }

  @DeleteMapping("/{ids}")
  void deleteClients(@PathVariable("ids") Collection<Long> ids) {
    service.deleteCustomers(ids);
  }
}

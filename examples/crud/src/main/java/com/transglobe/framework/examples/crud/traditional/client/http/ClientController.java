package com.transglobe.framework.examples.crud.traditional.client.http;

import com.transglobe.framework.examples.crud.traditional.client.service.ClientService;
import com.transglobe.framework.examples.crud.traditional.client.repository.ClientEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Traditional Style")
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
class ClientController {

  final ClientService service;

  @GetMapping
  Page<ClientEntity> listClients(ClientCriteria criteria, Pageable pageable) {
    return service.getAll(criteria, pageable);
  }

  @PostMapping
  Long createClient(@Validated @RequestBody ClientEntity user) {
    return service.createClient(user);
  }

  @PutMapping("/{id}")
  void replaceClient(@PathVariable("id") long id, @Validated @RequestBody ClientEntity user) {
    user.setId(id);
    service.replaceClient(user);
  }

  @DeleteMapping("/{ids}")
  void deleteClients(@PathVariable("ids") Collection<Long> ids) {
    service.deleteClients(ids);
  }
}

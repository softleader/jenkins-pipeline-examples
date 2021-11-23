package com.transglobe.framework.examples.crud.rose.customer.mapper;

import com.transglobe.framework.examples.crud.rose.customer.ChangePassword;
import com.transglobe.framework.examples.crud.rose.customer.CreateCustomer;
import com.transglobe.framework.examples.crud.rose.customer.CustomerDto;
import com.transglobe.framework.examples.crud.rose.customer.ReplaceCustomer;
import com.transglobe.framework.examples.crud.rose.customer.repository.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CustomerMapper {

  CustomerEntity toEntity(CreateCustomer cmd);

  CustomerDto fromEntity(CustomerEntity entity);

  CustomerEntity replace(ReplaceCustomer cmd, @MappingTarget CustomerEntity entity);

  CustomerEntity replace(ChangePassword cmd, @MappingTarget CustomerEntity entity);
}

package com.transglobe.framework.data.jpa.hibernate;

import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * @author Jason Wu
 */
public class CaseSensitiveSpringPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

  @Override
  protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
    return false;
  }
}

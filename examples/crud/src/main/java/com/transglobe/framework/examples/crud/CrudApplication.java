package com.transglobe.framework.examples.crud;

import com.transglobe.framework.core.TransGlobeApplication;
import com.transglobe.framework.core.TransGlobeBootstrap;

@TransGlobeBootstrap
public class CrudApplication {

  public static void main(String[] args) {
    TransGlobeApplication.run(CrudApplication.class, args);
  }
}

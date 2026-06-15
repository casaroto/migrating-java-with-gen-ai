package com.acme.modres.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

  // Migrated from servlets to JAX-RS: previously threw jakarta.servlet.ServletException.
  // Now throws an unchecked RuntimeException, which Quarkus REST maps to HTTP 500.
  public static void handleException(Exception e, String errorMsg, Logger logger) {
    if (e == null) {
      logger.severe(errorMsg);
      throw new RuntimeException(errorMsg);
    } else {
      logger.log(Level.SEVERE, errorMsg, e);
      throw new RuntimeException(errorMsg, e);
    }
  }
}

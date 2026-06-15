// Migrated from WelcomeServlet to Spring Web MVC.
package com.mvc.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

  // The legacy app applied two servlet filters (FirstFilter/SecondFilter) to
  // the welcome path that prepended greeting text. Their behavior is folded
  // here so the endpoint output is preserved without servlet filters.
  @GetMapping(value = "/welcome", produces = MediaType.TEXT_PLAIN_VALUE)
  public String welcome(@RequestParam(value = "user", required = false) String user) {
    String name = (user == null) ? "defaultUser" : user;
    return "Welcome " + name + " to our site! Enjoy!";
  }
}

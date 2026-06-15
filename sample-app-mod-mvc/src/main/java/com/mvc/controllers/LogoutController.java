// Migrated from LogoutServlet to Spring Web MVC.
package com.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

  @GetMapping("/logout")
  public String logout(HttpServletRequest request) {
    try {
      request.logout();
    } catch (Exception e) {
      System.err.println("[ERROR] Error logging out");
      e.printStackTrace();
    }

    // Context-relative redirect to the static login page.
    return "redirect:/login.jsp";
  }
}

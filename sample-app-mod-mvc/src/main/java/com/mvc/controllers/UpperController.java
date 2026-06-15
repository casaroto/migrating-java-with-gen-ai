// Migrated from UpperServlet to Spring Web MVC.
package com.mvc.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpperController {

  @GetMapping(value = "/upper", produces = MediaType.TEXT_HTML_VALUE)
  public String toUpper(@RequestParam(value = "input", required = false) String input) {
    String originalStr = (input == null) ? "" : input;

    String newStr = originalStr.toUpperCase();
    newStr = StringEscapeUtils.escapeHtml4(newStr);

    return "<br/><b>upper case input " + newStr + "</b>";
  }
}

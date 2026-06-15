package com.acme.modres;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Migrated from UpperServlet to a Jakarta REST (JAX-RS) resource.
 */
@Path("/resorts/upper")
public class UpperResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String toUpper(@QueryParam("input") String input) {
    String originalStr = (input == null) ? "" : input;

    // Replaces com.ibm.websphere.servlet.response.ResponseUtils.encodeDataString
    // (WebSphere-only API) with a portable HTML entity escape.
    String newStr = encodeDataString(originalStr.toUpperCase());

    return "<br/><b>upper case input " + newStr + "</b>";
  }

  private static String encodeDataString(String input) {
    if (input == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      switch (c) {
        case '&' -> sb.append("&amp;");
        case '<' -> sb.append("&lt;");
        case '>' -> sb.append("&gt;");
        case '"' -> sb.append("&quot;");
        case '\'' -> sb.append("&#39;");
        default -> sb.append(c);
      }
    }
    return sb.toString();
  }
}

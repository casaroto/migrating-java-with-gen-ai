package com.acme.modres;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

// Assisted by WCA for GP
// Latest GenAI contribution: granite-20B-code-instruct-v2 model
// Migrated from a servlet Filter (web.xml mapped to /welcome) to a JAX-RS
// ContainerResponseFilter. It decorates the welcome response with a greeting.
@Provider
public class FirstFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response) {
    if (!isWelcomePath(request)) {
      return;
    }
    Object entity = response.getEntity();
    if (!(entity instanceof String body)) {
      return;
    }
    String user = request.getUriInfo().getQueryParameters().getFirst("user");
    if (user == null) {
      user = "defaultUser";
    }
    response.setEntity("Welcome " + user + " " + body);
  }

  // UriInfo#getPath() may or may not include a leading slash depending on the
  // implementation, so normalize before comparing.
  static boolean isWelcomePath(ContainerRequestContext request) {
    String path = request.getUriInfo().getPath();
    return "resorts/welcome".equals(path) || "/resorts/welcome".equals(path);
  }
}

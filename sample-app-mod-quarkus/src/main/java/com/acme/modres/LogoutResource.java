package com.acme.modres;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;

/**
 * Migrated from LogoutServlet to a Jakarta REST (JAX-RS) resource.
 *
 * Container-managed FORM authentication was already disabled in the demo, so
 * this simply redirects back to the login page (as the servlet did).
 */
@Path("/logout")
public class LogoutResource {

  @GET
  public Response logout() {
    return Response.seeOther(URI.create("/login.jsp")).build();
  }
}

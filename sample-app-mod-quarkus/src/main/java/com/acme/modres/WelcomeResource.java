package com.acme.modres;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Migrated from WelcomeServlet to a Jakarta REST (JAX-RS) resource.
 *
 * The response is further decorated by FirstFilter and SecondFilter (migrated
 * from the web.xml-mapped servlet filters), which now run as JAX-RS response
 * filters bound to this path.
 */
@Path("/resorts/welcome")
public class WelcomeResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String welcome() {
    return "Enjoy!";
  }
}

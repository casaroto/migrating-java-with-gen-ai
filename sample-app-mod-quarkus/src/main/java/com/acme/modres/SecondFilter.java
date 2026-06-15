package com.acme.modres;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

//Assisted by WCA for GP
//Latest GenAI contribution: granite-20B-code-instruct-v2 model
// Migrated from a servlet Filter (web.xml mapped to /welcome) to a JAX-RS
// ContainerResponseFilter. It appends the "to our site!" greeting fragment.
@Provider
public class SecondFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response) {
    if (!FirstFilter.isWelcomePath(request)) {
      return;
    }
    Object entity = response.getEntity();
    if (!(entity instanceof String body)) {
      return;
    }
    // Order-independent w.r.t. FirstFilter: insert the fragment before "Enjoy!".
    response.setEntity(body.replace("Enjoy!", "to our site! Enjoy!"));
  }
}

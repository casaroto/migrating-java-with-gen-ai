package com.acme.modres;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ModResortsEndpointsTest {

  @Test
  void indexPageIsServed() {
    given()
        .when().get("/")
        .then()
        .statusCode(200)
        .contentType(containsString("text/html"));
  }

  @Test
  void welcomeReturnsGreetingDecoratedByFilters() {
    // FirstFilter adds "Welcome <user>", SecondFilter adds "to our site!",
    // and WelcomeResource provides "Enjoy!".
    given()
        .queryParam("user", "Rodrigo")
        .when().get("/resorts/welcome")
        .then()
        .statusCode(200)
        .body(equalTo("Welcome Rodrigo to our site! Enjoy!"));
  }

  @Test
  void weatherServletReturnsDefaultDataForKnownCity() {
    given()
        .queryParam("selectedCity", "Paris")
        .when().get("/resorts/weather")
        .then()
        .statusCode(200)
        .contentType(containsString("application/json"));
  }

  @Test
  void availabilityServletReturnsJson() {
    given()
        .queryParam("date", "12/25/2030")
        .when().get("/resorts/availability")
        .then()
        .statusCode(200)
        .body("availability", equalTo("true"));
  }

  @Test
  void upperServletUppercasesAndEscapesInput() {
    given()
        .queryParam("input", "hello <b>")
        .when().get("/resorts/upper")
        .then()
        .statusCode(200)
        .body(containsString("HELLO &lt;B&gt;"));
  }
}

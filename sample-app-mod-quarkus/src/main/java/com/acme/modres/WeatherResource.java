package com.acme.modres;

import com.acme.modres.db.ModResortsCustomerInformation;
import com.acme.modres.exception.ExceptionHandler;
import com.acme.modres.mbean.AppInfo;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 * Migrated from WeatherServlet. The servlet behavior is preserved as a Jakarta
 * REST (JAX-RS) resource. The AppInfo MBean that the servlet used to register in
 * init()/destroy() is now bound to the Quarkus application lifecycle.
 */
@Singleton
@Path("/resorts/weather")
public class WeatherResource {

  private static final Logger logger = Logger.getLogger(WeatherResource.class.getName());

  // local OS environment variable key name. The key value should provide an API
  // key that will be used to get weather information from site: http://www.wunderground.com
  private static final String WEATHER_API_KEY = "WEATHER_API_KEY";

  @Inject
  ModResortsCustomerInformation customerInfo;

  private MBeanServer server;
  private ObjectName weatherON;
  private ObjectInstance mbean;

  // Replaces WeatherServlet#init()
  void onStart(@Observes StartupEvent event) {
    server = ManagementFactory.getPlatformMBeanServer();
    try {
      weatherON = new ObjectName("com.acme.modres.mbean:name=appInfo");
      mbean = server.registerMBean(new AppInfo(), weatherON);
    } catch (MalformedObjectNameException | InstanceAlreadyExistsException
        | MBeanRegistrationException | NotCompliantMBeanException e) {
      e.printStackTrace();
    }
  }

  // Replaces WeatherServlet#destroy()
  void onStop(@Observes ShutdownEvent event) {
    if (mbean != null && weatherON != null) {
      try {
        server.unregisterMBean(weatherON);
      } catch (MBeanRegistrationException | InstanceNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getWeather(@QueryParam("selectedCity") String city) {
    logger.log(Level.FINE, "requested city is " + city);

    String weatherAPIKey = System.getenv(WEATHER_API_KEY);
    logger.log(Level.FINE, "weatherAPIKey is " + mockKey(weatherAPIKey));

    if (weatherAPIKey != null && weatherAPIKey.trim().length() > 0) {
      logger.info("weatherAPIKey is found, system will provide the real time weather data for the city " + city);
      return Response.ok(getRealTimeWeatherData(city, weatherAPIKey)).build();
    } else {
      logger.info(
          "weatherAPIKey is not found, will provide the weather data dated August 10th, 2018 for the city " + city);
      return Response.ok(getDefaultWeatherData(city)).build();
    }
  }

  private String getRealTimeWeatherData(String city, String apiKey) {
    String resturl = null;
    String resturlbase = Constants.WUNDERGROUND_API_PREFIX + apiKey + Constants.WUNDERGROUND_API_PART;

    if (Constants.PARIS.equals(city)) {
      resturl = resturlbase + "France/Paris.json";
    } else if (Constants.LAS_VEGAS.equals(city)) {
      resturl = resturlbase + "NV/Las_Vegas.json";
    } else if (Constants.SAN_FRANCISCO.equals(city)) {
      resturl = resturlbase + "/CA/San_Francisco.json";
    } else if (Constants.MIAMI.equals(city)) {
      resturl = resturlbase + "FL/Miami.json";
    } else if (Constants.CORK.equals(city)) {
      resturl = resturlbase + "ireland/cork.json";
    } else if (Constants.BARCELONA.equals(city)) {
      resturl = resturlbase + "Spain/Barcelona.json";
    } else {
      String errorMsg = "Sorry, the weather information for your selected city: " + city +
          " is not available.  Valid selections are: " + Constants.SUPPORTED_CITIES;
      ExceptionHandler.handleException(null, errorMsg, logger);
    }

    HttpURLConnection con = null;
    try {
      URL obj = new URL(resturl);
      con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
    } catch (MalformedURLException e1) {
      ExceptionHandler.handleException(e1, "Caught MalformedURLException. Please make sure the url is correct.", logger);
    } catch (ProtocolException e2) {
      ExceptionHandler.handleException(e2,
          "Caught ProtocolException: " + e2.getMessage() + ". Not able to set request method to http connection.",
          logger);
    } catch (IOException e3) {
      ExceptionHandler.handleException(e3,
          "Caught IOException: " + e3.getMessage() + ". Not able to open connection.", logger);
    }

    try {
      int responseCode = con.getResponseCode();
      logger.log(Level.FINEST, "Response Code: " + responseCode);

      if (responseCode >= 200 && responseCode < 300) {
        StringBuilder responseStr = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            responseStr.append(inputLine);
          }
        }
        logger.log(Level.FINE, "responseStr: " + responseStr);
        return responseStr.toString();
      } else {
        ExceptionHandler.handleException(null,
            "REST API call " + resturl + " returns an error response: " + responseCode, logger);
      }
    } catch (IOException e) {
      ExceptionHandler.handleException(e, "Problem occured when processing the weather server response.", logger);
    }
    return ""; // unreachable: handleException always throws on the error paths
  }

  private String getDefaultWeatherData(String city) {
    try {
      DefaultWeatherData defaultWeatherData = new DefaultWeatherData(city);
      return defaultWeatherData.getDefaultWeatherData();
    } catch (UnsupportedOperationException e) {
      ExceptionHandler.handleException(e, e.getMessage(), logger);
    } catch (Exception e) {
      ExceptionHandler.handleException(e, "Problem occured when getting the default weather data.", logger);
    }
    return ""; // unreachable
  }

  private static String mockKey(String toBeMocked) {
    if (toBeMocked == null) {
      return null;
    }
    String lastToKeep = toBeMocked.substring(toBeMocked.length() - 3);
    return "*********" + lastToKeep;
  }
}

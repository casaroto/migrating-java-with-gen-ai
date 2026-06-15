package com.acme.modres;

import com.acme.modres.mbean.IOUtils;
import com.acme.modres.mbean.reservation.Reservation;
import com.acme.modres.mbean.reservation.ReservationCheckerData;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Migrated from AvailabilityCheckerServlet to a Jakarta REST (JAX-RS) resource.
 */
@Path("/resorts/availability")
public class AvailabilityCheckerResource {

  private static final Logger logger = Logger.getLogger(AvailabilityCheckerResource.class.getName());

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkAvailability(@QueryParam("date") String selectedDateStr) {
    int statusCode = 200;

    // load reserved dates (previously cached in the servlet's init())
    ReservationCheckerData reservationCheckerData =
        new ReservationCheckerData(IOUtils.getReservationListFromConfig());

    boolean parsedDate = reservationCheckerData.setSelectedDate(selectedDateStr);
    if (!parsedDate || reservationCheckerData.getReservationList() == null) {
      statusCode = 500;
      reservationCheckerData.setAvailablility(false);
    } else {
      List<Reservation> reservations = reservationCheckerData.getReservationList().getReservations();
      boolean isAvailible = true;

      for (Reservation reservation : reservations) {
        try {
          Date fromDate = new SimpleDateFormat(Constants.DATA_FORMAT).parse(reservation.getFromDate());
          Date toDate = new SimpleDateFormat(Constants.DATA_FORMAT).parse(reservation.getToDate());
          Date selectedDate = reservationCheckerData.getSelectedDate();

          if (selectedDate.after(fromDate) && selectedDate.before(toDate)) {
            isAvailible = false;
            break;
          }
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
      }

      reservationCheckerData.setAvailablility(isAvailible);

      // Adjust the status code based on availability
      if (!isAvailible) {
        statusCode = 201;
      }
    }

    String body = "{\"availability\": \"" + String.valueOf(reservationCheckerData.isAvailible()) + "\"}";
    return Response.status(statusCode).entity(body).build();
  }
}

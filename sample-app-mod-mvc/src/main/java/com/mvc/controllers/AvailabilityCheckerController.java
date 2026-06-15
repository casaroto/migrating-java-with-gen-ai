// Assisted by watsonx Code Assistant 
package com.mvc.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.modres.mbean.IOUtils;
import com.acme.modres.mbean.reservation.DateChecker;
import com.acme.modres.mbean.reservation.ReservationCheckerData;
import com.acme.modres.mbean.reservation.Reservation;

import com.acme.modres.util.ZipValidator;

import com.acme.modres.Constants;

@RestController
public class AvailabilityCheckerController {

  private static final Logger logger = Logger.getLogger(AvailabilityCheckerController.class.getName());

  private ReservationCheckerData reservationCheckerData;

  @PostConstruct
  public void init() {
    // load reserved dates
    this.reservationCheckerData = new ReservationCheckerData(IOUtils.getReservationListFromConfig());
  }

  @GetMapping("/availability")
  public void getAvailability(
      @RequestParam(value = "date", required = false) String selectedDateStr,
      HttpServletResponse response)
      throws ServletException, IOException {

    String methodName = "getAvailability";
    logger.entering(AvailabilityCheckerController.class.getName(), methodName);
    int statusCode = 200;

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

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().print("{\"availability\": \"" + String.valueOf(reservationCheckerData.isAvailible()) + "\"}");
    response.setStatus(statusCode);
  }

  @PostMapping("/availability")
  public int exportRevervations(@RequestParam(value = "date", required = false) String selectedDateStr)
      throws IOException {

    File fileToZip = IOUtils.getFileFromRelativePath("reservations.json");
    String userDirectory = System.getProperty("user.home");
    String zipPath = userDirectory + "/reservations.zip";

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(zipPath);
      ZipOutputStream zipOut = new ZipOutputStream(fos);

      FileInputStream fis = new FileInputStream(fileToZip);
      ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
      zipOut.putNextEntry(zipEntry);

      byte[] bytes = new byte[1024];
      int length;
      while ((length = fis.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }
      fis.close();

      zipOut.close();
      fos.close();

      // verify zip
      ZipValidator zipValidator = new ZipValidator(new File(zipPath));
      if (zipValidator.isValid()) {
        return 0;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return -1;
  }
}

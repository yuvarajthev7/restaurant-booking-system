package com.restaurant_booking.controller;

import com.restaurant_booking.dto.ReservationRequest;
import com.restaurant_booking.model.Reservation;
import com.restaurant_booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @PostMapping
  public Reservation makeReservation(@RequestBody ReservationRequest request, Principal principal) {
      // principal.getName() will now return the Government ID
      return reservationService.makeReservation(
          principal.getName(),
          request.getTableId(),
          request.getReservationTime()
      );
  }

  @GetMapping
  public List<Reservation> getAllReservations() {
      return reservationService.getAllReservations();
  }
  @DeleteMapping("/{id}")
  public void deleteReservation(@PathVariable Long id) {
      reservationService.deleteReservation(id);
  }
}

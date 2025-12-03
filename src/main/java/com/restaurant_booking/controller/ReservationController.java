package com.restaurant_booking.controller;

import com.restaurant_booking.dto.ReservationRequest;
import com.restaurant_booking.model.Reservation;
import com.restaurant_booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation makeReservation(@RequestBody ReservationRequest request) {
        return reservationService.makeReservation(
            request.getUserId(),
            request.getTableId(),
            request.getReservationTime()
        );
    }
}

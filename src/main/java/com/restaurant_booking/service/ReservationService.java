package com.restaurant_booking.service;

import com.restaurant_booking.model.*;
import com.restaurant_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RestaurantTableRepository tableRepository;

    public Reservation makeReservation(Long userId, Long tableId, LocalDateTime time) {
        //finding the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //finding the table
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        //create the reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTable(table);
        reservation.setReservationTime(time);

        return reservationRepository.save(reservation);
    }
}

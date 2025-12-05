package com.restaurant_booking.service;

import com.restaurant_booking.model.*;
import com.restaurant_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RestaurantTableRepository tableRepository;

    // Updated: Takes "String userEmail" instead of "Long userId"
    public Reservation makeReservation(String govId, Long tableId, LocalDateTime time) {
        User user = userRepository.findByGovernmentId(govId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // ... (rest of the logic remains the same: find table, save reservation)
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTable(table);
        reservation.setReservationTime(time);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

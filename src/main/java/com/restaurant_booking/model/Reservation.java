package com.restaurant_booking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who booked it?
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Which table?
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    // When?
    private LocalDateTime reservationTime;
}

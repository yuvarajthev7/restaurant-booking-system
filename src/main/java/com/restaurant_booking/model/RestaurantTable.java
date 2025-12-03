package com.restaurant_booking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber; // e.g., Table #5
    private int seatCount;   // e.g., 4 seats

    // Relationship: Many Tables -> One Restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id") // The foreign key in the DB
    @JsonIgnore
    private Restaurant restaurant;
}

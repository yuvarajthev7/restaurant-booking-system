package com.restaurant_booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    private Long userId;
    private Long tableId;
    private LocalDateTime reservationTime;
}

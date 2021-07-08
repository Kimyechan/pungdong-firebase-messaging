package com.diving.firebasemessaging.kafka.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCancelInfo {
    private String instructorId;
    private String lectureId;
    private String scheduleId;
    private String messageBody;
}

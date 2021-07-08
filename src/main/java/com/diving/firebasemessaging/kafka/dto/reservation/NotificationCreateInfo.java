package com.diving.firebasemessaging.kafka.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateInfo {
    private String title;
    private String body;
    private List<String> applicantIds;
    private String lectureId;
}

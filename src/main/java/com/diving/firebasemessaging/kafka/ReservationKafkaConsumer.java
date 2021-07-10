package com.diving.firebasemessaging.kafka;

import com.diving.firebasemessaging.domain.FirebaseToken;
import com.diving.firebasemessaging.exception.ResourceNotFoundException;
import com.diving.firebasemessaging.kafka.dto.reservation.NotificationCreateInfo;
import com.diving.firebasemessaging.kafka.dto.reservation.ReservationCancelInfo;
import com.diving.firebasemessaging.kafka.dto.reservation.ReservationCreateInfo;
import com.diving.firebasemessaging.repo.FirebaseTokenJpaRepo;
import com.diving.firebasemessaging.service.FirebaseMessageService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationKafkaConsumer {
    private final FirebaseTokenJpaRepo firebaseTokenJpaRepo;
    private final FirebaseMessageService firebaseMessageService;

    @KafkaListener(topics = "create-reservation")
    public void consumeReservationCreateInfo(ReservationCreateInfo info) throws IOException, FirebaseMessagingException {
        FirebaseToken firebaseToken = firebaseTokenJpaRepo.findById(Long.valueOf(info.getInstructorId()))
                .orElseThrow(ResourceNotFoundException::new);

        String firebaseTokenValue = firebaseToken.getToken();
        String title = "강의를 예약했습니다.";

        Map<String, String> allData = new HashMap<>();
        allData.put("lectureId", info.getLectureId());
        allData.put("scheduleId", info.getScheduleId());

        firebaseMessageService.sendSingleMessage(firebaseTokenValue, title, info.getMessageBody(), allData);
    }

    @KafkaListener(topics = "cancel-reservation")
    public void consumeReservationCancelInfo(ReservationCancelInfo info) throws IOException, FirebaseMessagingException {
        FirebaseToken firebaseToken = firebaseTokenJpaRepo.findById(Long.valueOf(info.getInstructorId()))
                .orElseThrow(ResourceNotFoundException::new);

        String firebaseTokenValue = firebaseToken.getToken();
        String title = "강의 예약을 취소했습니다.";

        Map<String, String> allData = new HashMap<>();
        allData.put("lectureId", info.getLectureId());
        allData.put("scheduleId", info.getScheduleId());

        firebaseMessageService.sendSingleMessage(firebaseTokenValue, title, info.getMessageBody(), allData);
    }

    @KafkaListener(topics = "lecture-notification")
    public void consumeLectureNotification(NotificationCreateInfo info) throws IOException, FirebaseMessagingException {
        List<String> firebaseTokens = new ArrayList<>();
        for (String applicantId : info.getApplicantIds()) {
            FirebaseToken firebaseToken = firebaseTokenJpaRepo.findById(Long.valueOf(applicantId))
                    .orElseThrow(ResourceNotFoundException::new);
            firebaseTokens.add(firebaseToken.getToken());
        }

        Map<String, String> allData = new HashMap<>();
        allData.put("lectureId", info.getLectureId());

        firebaseMessageService.sendMulticastMessage(firebaseTokens, info.getTitle(), info.getBody(), allData);
    }
}

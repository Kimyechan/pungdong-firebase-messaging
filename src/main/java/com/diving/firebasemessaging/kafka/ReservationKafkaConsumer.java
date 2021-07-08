package com.diving.firebasemessaging.kafka;

import com.diving.firebasemessaging.domain.FirebaseToken;
import com.diving.firebasemessaging.kafka.dto.reservation.ReservationCancelInfo;
import com.diving.firebasemessaging.kafka.dto.reservation.ReservationCreateInfo;
import com.diving.firebasemessaging.repo.FirebaseTokenJpaRepo;
import com.diving.firebasemessaging.service.FirebaseMessageService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationKafkaConsumer {
    private final FirebaseTokenJpaRepo firebaseTokenJpaRepo;
    private final FirebaseMessageService firebaseMessageService;

    @KafkaListener(topics = "create-reservation")
    public void consumeReservationCreateInfo(ReservationCreateInfo info) throws IOException, FirebaseMessagingException {
        FirebaseToken firebaseToken = firebaseTokenJpaRepo.findById(Long.valueOf(info.getInstructorId()))
                .orElse(null);

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
                .orElse(null);

        String firebaseTokenValue = firebaseToken.getToken();
        String title = "강의 예약을 취소했습니다.";

        Map<String, String> allData = new HashMap<>();
        allData.put("lectureId", info.getLectureId());
        allData.put("scheduleId", info.getScheduleId());

        firebaseMessageService.sendSingleMessage(firebaseTokenValue, title, info.getMessageBody(), allData);
    }
}

package com.diving.firebasemessaging.kafka;

import com.diving.firebasemessaging.domain.FirebaseToken;
import com.diving.firebasemessaging.repo.FirebaseTokenJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseTokenConsumer {
    private final FirebaseTokenJpaRepo firebaseTokenJpaRepo;

    @KafkaListener(topics = "firebase-token", groupId = "group_id")
    public void consume(FirebaseTokenInfo firebaseTokenInfo) {
        FirebaseToken firebaseToken = FirebaseToken.builder()
                .id(Long.valueOf(firebaseTokenInfo.getId()))
                .token(firebaseTokenInfo.getToken())
                .build();

        firebaseTokenJpaRepo.save(firebaseToken);
    }
}

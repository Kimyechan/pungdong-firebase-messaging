package com.diving.firebasemessaging.kafka;

import com.diving.firebasemessaging.domain.FirebaseToken;
import com.diving.firebasemessaging.kafka.dto.comment.CommentCreateInfo;
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
public class CommentKafkaConsumer {
    private final FirebaseTokenJpaRepo firebaseTokenJpaRepo;
    private final FirebaseMessageService firebaseMessageService;

    @KafkaListener(topics = "create-comment")
    public void consumeCommentCreateEvent(CommentCreateInfo info) throws IOException, FirebaseMessagingException {
        String title = "회원님이 작성하신 글에 댓글이 달렸습니다.";
        String body = info.getCommentWriterNickname() + "님이 " + info.getPostTitle() + " 게시글에 댓글을 달았습니다";

        FirebaseToken token = firebaseTokenJpaRepo.findById(Long.valueOf(info.getPostWriterId())).orElseThrow(null);
        String tokenValue = token.getToken();

        Map<String, String> allData = new HashMap<>();
        allData.put("postId", info.getPostId());

        firebaseMessageService.sendSingleMessage(tokenValue, title, body, allData);
    }
}

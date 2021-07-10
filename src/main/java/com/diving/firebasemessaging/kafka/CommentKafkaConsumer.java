package com.diving.firebasemessaging.kafka;

import com.diving.firebasemessaging.domain.FirebaseToken;
import com.diving.firebasemessaging.exception.ResourceNotFoundException;
import com.diving.firebasemessaging.kafka.dto.comment.CommentCommentCreateInfo;
import com.diving.firebasemessaging.kafka.dto.comment.CommentCreateInfo;
import com.diving.firebasemessaging.repo.FirebaseTokenJpaRepo;
import com.diving.firebasemessaging.service.FirebaseMessageService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentKafkaConsumer {
    private final FirebaseTokenJpaRepo firebaseTokenJpaRepo;
    private final FirebaseMessageService firebaseMessageService;
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @KafkaListener(topics = "create-comment")
    public void consumeCommentCreateEvent(CommentCreateInfo info) throws IOException, FirebaseMessagingException {
        String title = "회원님이 작성하신 글에 댓글이 달렸습니다.";
        String body = info.getCommentWriterNickname() + "님이 " + info.getPostTitle() + " 게시글에 댓글을 달았습니다";

        FirebaseToken token = firebaseTokenJpaRepo.findById(Long.valueOf(info.getPostWriterId())).orElseThrow(ResourceNotFoundException::new);
        String tokenValue = token.getToken();

        Map<String, String> allData = new HashMap<>();
        allData.put("postId", info.getPostId());

        firebaseMessageService.sendSingleMessage(tokenValue, title, body, allData);
    }

    @KafkaListener(topics = "create-comment-comment")
    public void consumeCommentCommentCreateEvent(CommentCommentCreateInfo info) throws IOException, FirebaseMessagingException {
        String title = "댓글이 달렸습니다.";
        String body = info.getCommentCommentWriterNickname() + "님이 " + "댓글을 달았습니다";

        FirebaseToken postWriterToken = firebaseTokenJpaRepo.findById(Long.valueOf(info.getPostWriterId())).orElseThrow(ResourceNotFoundException::new);
        FirebaseToken commentWriterToken = firebaseTokenJpaRepo.findById(Long.valueOf(info.getCommentWriterId())).orElseThrow(ResourceNotFoundException::new);

        List<String> tokenValues = new ArrayList<>();
        tokenValues.add(postWriterToken.getToken());
        tokenValues.add(commentWriterToken.getToken());

        Map<String, String> allData = new HashMap<>();
        allData.put("postId", info.getPostId());

        firebaseMessageService.sendMulticastMessage(tokenValues, title, body, allData);
    }

    @KafkaListener(topics = "create-comment.DLT")
    public void createCommentDltListen(CommentCreateInfo in) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        logger.info("Received from DLT: " + in.getPostTitle());
    }
}

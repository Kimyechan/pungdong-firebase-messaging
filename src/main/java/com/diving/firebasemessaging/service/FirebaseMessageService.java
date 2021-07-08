package com.diving.firebasemessaging.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FirebaseMessageService {

    public void provideUserAuthInfo() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public String sendSingleMessage(String registrationToken,
                                    String title,
                                    String body,
                                    Map<String, String> allData) throws FirebaseMessagingException, IOException {
        provideUserAuthInfo();

        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putAllData(allData)
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    public void sendMulticastMessage(List<String> registrationTokens,
                                     String title,
                                     String body,
                                     Map<String, String> allData) throws IOException, FirebaseMessagingException {
        provideUserAuthInfo();

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("score", "850")
                .putData("time", "2:45")
                .addAllTokens(registrationTokens)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(registrationTokens.get(i));
                }
            }

            System.out.println("List of tokens that caused failures: " + failedTokens);
        }
    }
}

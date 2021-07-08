package com.diving.firebasemessaging.kafka.dto.firebaseToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseTokenInfo {
    private String id;
    private String token;
}

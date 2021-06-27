package com.diving.firebasemessaging.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class FirebaseToken {
    @Id
    private Long id;

    private String token;
}

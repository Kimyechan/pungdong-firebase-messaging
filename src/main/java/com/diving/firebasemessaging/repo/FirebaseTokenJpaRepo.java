package com.diving.firebasemessaging.repo;

import com.diving.firebasemessaging.domain.FirebaseToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirebaseTokenJpaRepo extends JpaRepository<FirebaseToken, Long> {

}

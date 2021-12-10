package com.project.cardoc.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("새로운 사용자 저장")
    public void saveUserTest(){
        // given
        User user = User.builder()
                .name("test01")
                .password("testpassword")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        Assertions.assertEquals(user.getName(), savedUser.getName());
        Assertions.assertEquals(user.getPassword(), savedUser.getPassword());
        Assertions.assertEquals(user.getRoles(), savedUser.getRoles());

    }
}

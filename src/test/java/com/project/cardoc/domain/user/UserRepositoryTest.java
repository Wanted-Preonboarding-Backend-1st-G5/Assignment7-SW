package com.project.cardoc.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

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

    @Test
    @DisplayName("이름으로 사용자 찾기 성공")
    public void findByNameSuccessTest(){
        //given
        User user = User.builder()
                .name("test01")
                .password("testpassword")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        User savedUser = userRepository.save(user);

        //when
        Optional<User> userFindByName = userRepository.findByName(user.getName());

        //then
        userFindByName.ifPresent(
                value -> Assertions.assertEquals(savedUser.getName(), value.getName())
        );
    }

    @Test
    @DisplayName("이름으로 사용자 찾기 실패 - 해당 이름의 사용자가 없음")
    public void findByNameFailTest(){
        //given

        //when
        Optional<User> userFindByName = userRepository.findByName("test01");

        //then
        Assertions.assertEquals(Optional.empty(), userFindByName);
    }

    @Test
    @DisplayName("이름으로 해당 사용자 존재 여부 확인 성공")
    public void verifyingExistenceOfUserByNameSuccessTest(){
        //given
        User user = User.builder()
                .name("test01")
                .password("testpassword")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);

        //when
        boolean existenceOfUser = userRepository.existsByName(user.getName());

        //then
        Assertions.assertEquals(true, existenceOfUser);
    }
}

package com.project.cardoc.service;

import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.user.User;
import com.project.cardoc.domain.user.UserRepository;
import com.project.cardoc.domain.usertire.UserTire;
import com.project.cardoc.domain.usertire.UserTireRepository;

import com.project.cardoc.exception.BadRequestException;
import com.project.cardoc.exception.ResourceNotFoundException;

import com.project.cardoc.payload.request.UserRequest;
import com.project.cardoc.payload.response.TireResponse;
import com.project.cardoc.payload.response.UserResponse;

import com.project.cardoc.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTireRepository userTireRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void createNewUser(UserRequest userRequest){
        if(checkNameDuplicate(userRequest.getId())){
            throw new BadRequestException("중복된 ID 입니다");
        }

        userRepository.save(User.builder()
                .name(userRequest.getId())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))  //최초 가입 시 USER로 설정
                .build());
    }

    public boolean checkNameDuplicate(String name){
        return userRepository.existsByName(name);
    }

    public UserResponse login(UserRequest userRequest){
        User user = findUserFromName(userRequest.getId());

        if(!validatePassword(userRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("잘못된 비밀번호 입니다.");
        }

        String accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return UserResponse.of(user.getId(), accessToken);
    }

    public User findUserFromName(String name){
        return userRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("User", "name", name));
    }

    public boolean validatePassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<TireResponse> getTireListOwnedByUser(String userName) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "name", userName));

        List<UserTire> userTireList = userTireRepository.findAllByUser(user);

        List<TireResponse> tireResponseList = new ArrayList<>();
        for(UserTire userTire : userTireList){
            Tire tire = userTire.getTire();
            tireResponseList.add(TireResponse.fromEntity(tire));
        }

        return tireResponseList;
    }
}

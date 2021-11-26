package com.project.cardoc.controller;

import com.project.cardoc.payload.DefaultResponse;
import com.project.cardoc.payload.ResponseCode;
import com.project.cardoc.payload.request.UserRequest;
import com.project.cardoc.payload.response.UserResponse;
import com.project.cardoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.project.cardoc.payload.ResponseMessage.*;

@RequestMapping("users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserRequest userRequest){
        userService.createNewUser(userRequest);
        return ResponseEntity.ok()
                .body(DefaultResponse.of(ResponseCode.OK, SUCCESS_USER_SIGNUP));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userService.login(userRequest);
        return ResponseEntity.ok()
                .body(DefaultResponse.of(ResponseCode.OK, SUCCESS_USER_LOGIN, userResponse));
    }
}

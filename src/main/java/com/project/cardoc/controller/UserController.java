package com.project.cardoc.controller;

import com.project.cardoc.payload.DefaultResponse;
import com.project.cardoc.payload.ResponseCode;
import com.project.cardoc.payload.ResponseMessage;
import com.project.cardoc.payload.request.UserRequest;
import com.project.cardoc.payload.response.TireResponse;
import com.project.cardoc.payload.response.UserResponse;
import com.project.cardoc.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody @Valid final UserRequest userRequest) {
    userService.createNewUser(userRequest);
    return ResponseEntity.ok()
        .body(DefaultResponse.of(ResponseCode.OK, ResponseMessage.SUCCESS_USER_SIGNUP));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid final UserRequest userRequest) {
    UserResponse userResponse = userService.login(userRequest);
    return ResponseEntity.ok()
        .body(DefaultResponse
            .of(ResponseCode.OK, ResponseMessage.SUCCESS_USER_LOGIN, userResponse));
  }

  @GetMapping("/{userName}/tires")
  public ResponseEntity<?> getTireListOwnedByUser(@PathVariable("userName") final String userName) {
    List<TireResponse> tireResponseList = userService.getTireListOwnedByUser(userName);
    return ResponseEntity.ok()
        .body(DefaultResponse
            .of(ResponseCode.OK, ResponseMessage.SUCCESS_GET_TIRE_LIST, tireResponseList));
  }

}

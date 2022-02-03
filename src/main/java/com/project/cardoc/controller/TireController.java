package com.project.cardoc.controller;

import com.project.cardoc.payload.DefaultResponse;
import com.project.cardoc.payload.ResponseCode;
import com.project.cardoc.payload.ResponseMessage;
import com.project.cardoc.payload.request.UserTireRequest;
import com.project.cardoc.service.TireService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("tires")
@RequiredArgsConstructor
@RestController
public class TireController {

  private final TireService tireService;

  @PostMapping
  public ResponseEntity<?> saveTireInfoOwnedByUser(@RequestBody final List<UserTireRequest> userTires)
      throws ParseException {
    tireService.saveTireInfoOwnedByUser(userTires);
    return ResponseEntity.ok()
        .body(DefaultResponse.of(ResponseCode.OK, ResponseMessage.SUCCESS_SAVE_TIRE_INFO));
  }

}

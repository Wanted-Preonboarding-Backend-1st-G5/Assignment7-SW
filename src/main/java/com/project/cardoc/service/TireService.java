package com.project.cardoc.service;

import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.tire.TireRepository;
import com.project.cardoc.domain.user.User;
import com.project.cardoc.domain.user.UserRepository;
import com.project.cardoc.domain.usertire.UserTire;
import com.project.cardoc.domain.usertire.UserTireRepository;
import com.project.cardoc.exception.BadRequestException;
import com.project.cardoc.exception.ResourceNotFoundException;
import com.project.cardoc.payload.ResponseMessage;
import com.project.cardoc.payload.request.UserTireRequest;
import com.project.cardoc.parser.Json;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TireService {

  private final TireRepository tireRepository;
  private final UserRepository userRepository;
  private final UserTireRepository userTireRepository;

  private final RestTemplate restTemplate;

  @Transactional
  public void saveTireInfoOwnedByUser(final List<UserTireRequest> userTires) throws ParseException {
    List<UserTire> userTireList = new ArrayList<>();

    // 요청 데이터 개수 확인
    int dataSize = userTires.size();
    if (dataSize == 0 || dataSize > 5) {
      throw new BadRequestException(
          ResponseMessage.FAIL_USERTIRE_NOT_VALID_NUMBER_OF_DATA_REQUESTS);
    }

    for (UserTireRequest requestData : userTires) {
      User user = userRepository.findByName(requestData.getId())
          .orElseThrow(() -> new ResourceNotFoundException("User", "name", requestData.getId()));

      // 자동차 정보 조회 API 응답 상태코드 200인지 확인
      String trimId = requestData.getTrimId();
      String carInfoApiUrl = "https://dev.mycar.cardoc.co.kr/v1/trim/" + trimId;

      ResponseEntity<String> result;
      try {
        result = restTemplate.getForEntity(carInfoApiUrl, String.class);
      } catch (Exception e) {
        throw new BadRequestException(
            "trimID: " + trimId + ResponseMessage.FAIL_USERTIRE_CANNOT_FIND_CAR_INFO);
      }

      String[] tires = getTireInfoFromResult(result.getBody());
      createUserTireList(user, tires, userTireList);
    }

    // UserTire 객체 생성
    for (UserTire userTire : userTireList) {
      userTireRepository.save(userTire);
    }
  }

  public String[] getTireInfoFromResult(final String resultBody) throws ParseException {
    JSONObject jsonObject = Json.stringToJsonObject(resultBody);

    try {
      JSONObject spec = (JSONObject) jsonObject.get("spec");
      JSONObject driving = (JSONObject) spec.get("driving");
      JSONObject frontTire = (JSONObject) driving.get("frontTire");
      JSONObject rearTire = (JSONObject) driving.get("rearTire");

      String frontTireVal = frontTire.get("value").toString();
      String rearTireVal = rearTire.get("value").toString();

      frontTireVal = frontTireVal.replaceAll("\\s", "");  // 공백제거 : \t, \n, \x0B, \f, \r 모두 제거
      rearTireVal = rearTireVal.replaceAll("\\s", "");    // 공백제거 : \t, \n, \x0B, \f, \r 모두 제거

      if (frontTireVal.equals(rearTireVal)) {
        return new String[]{frontTireVal};
      } else {
        return new String[]{frontTireVal, rearTireVal};
      }
    } catch (Exception e) {
      throw new BadRequestException(ResponseMessage.FAIL_USERTIRE_CANNOT_FIND_TIRE_INFO);
    }
  }

  public void saveUserTireList(final String tireInfo, final User user,
      final List<UserTire> userTireList) {
    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(tireInfo);

    // 폭, 편평비, 휠사이즈 값 추출
    int[] tires = new int[3];
    for (int i = 0; i < tires.length; i++) {
      matcher.find();
      tires[i] = Integer.parseInt(matcher.group());
    }

    Tire tire = tireRepository
        .findByWidthAndAspectRatioAndWheelDiameter(tires[0], tires[1], tires[2]);
    if (tire == null) {
      tire = tireRepository.save(Tire.builder()
          .width(tires[0])
          .aspectRatio(tires[1])
          .wheelDiameter(tires[2])
          .build());
    }

    if (!userTireRepository.existsByUserAndTire(user, tire)) {
      UserTire userTire = UserTire.builder()
          .user(user)
          .tire(tire)
          .build();
      userTireList.add(userTire);
    }
  }

  public void createUserTireList(final User user, final String[] tires,
      final List<UserTire> userTireList) {
    String correctDataFormat = "[a-zA-Z]?[0-9]+/[0-9]+R[0-9]+";  // 올바른 데이터 형식 정규식 표현 : {차량종류}{폭}/{편평비}R{휠사이즈}

    for (String tireInfo : tires) {
      if (tireInfo.matches(correctDataFormat)) {    // 올바른 데이터 형식인 경우
        saveUserTireList(tireInfo, user, userTireList);
      }
    }
  }
}

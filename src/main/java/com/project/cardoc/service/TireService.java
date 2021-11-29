package com.project.cardoc.service;

import com.project.cardoc.domain.tire.Tire;
import com.project.cardoc.domain.tire.TireRepository;
import com.project.cardoc.domain.user.User;
import com.project.cardoc.domain.user.UserRepository;
import com.project.cardoc.domain.usertire.UserTire;
import com.project.cardoc.domain.usertire.UserTireRepository;

import com.project.cardoc.exception.BadRequestException;
import com.project.cardoc.exception.ResourceNotFoundException;

import com.project.cardoc.payload.request.UserTireRequest;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TireService {

    private final TireRepository        tireRepository;
    private final UserRepository        userRepository;
    private final UserTireRepository    userTireRepository;

    private final RestTemplate          restTemplate;

    @Transactional
    public void saveTireInfoOwnedByUser(List<UserTireRequest> userTires) throws ParseException {
        List<UserTire> userTireList = new ArrayList<>();

        // 요청 데이터 개수 확인
        int dataSize = userTires.size();
        if(dataSize == 0 || dataSize > 5)
            throw new BadRequestException("요청 값의 개수는 1개 이상 5개 이하여야 합니다.");

        for (UserTireRequest requestData : userTires) {
            User user = userRepository.findByName(requestData.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "name", requestData.getId()));

            // 자동차 정보 조회 API 응답 상태코드 200인지 확인
            String trimId = requestData.getTrimId();
            String carInfoApiURL = "https://dev.mycar.cardoc.co.kr/v1/trim/" + trimId;

            ResponseEntity<String> result;
            try{
                result = restTemplate.getForEntity(carInfoApiURL, String.class);
            } catch (Exception e){
                throw new BadRequestException("trimID: " + trimId + " 해당 자동차 정보를 조회할 수 없습니다");
            }

            String[] tires = getTireInfoFromResult(result.getBody());
            createUserTireList(user, tires, userTireList);
        }

        // UserTire 객체 생성
        for(UserTire userTire : userTireList)
            userTireRepository.save(userTire);
    }

    public String[] getTireInfoFromResult(String resultBody) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(resultBody);

        try{
            JSONObject spec         = (JSONObject) jsonObject.get("spec");
            JSONObject driving      = (JSONObject) spec.get("driving");
            JSONObject frontTire    = (JSONObject) driving.get("frontTire");
            JSONObject rearTire     = (JSONObject) driving.get("rearTire");

            String frontTireVal     = frontTire.get("value").toString();
            String rearTireVal      = rearTire.get("value").toString();

            frontTireVal = frontTireVal.replaceAll("\\s", "");  // 공백제거 : \t, \n, \x0B, \f, \r 모두 제거
            rearTireVal = rearTireVal.replaceAll("\\s", "");    // 공백제거 : \t, \n, \x0B, \f, \r 모두 제거

            if(frontTireVal.equals(rearTireVal))
                return new String[]{frontTireVal};
            else
                return new String[]{frontTireVal, rearTireVal};
        } catch (Exception e){
            throw new BadRequestException("해당 값을 찾을 수 없습니다");
        }
    }

    public void createUserTireList(User user, String[] tires, List<UserTire> userTireList){
        String correctDataFormat = "[a-zA-Z]?[0-9]+/[0-9]+R[0-9]+";  // 올바른 데이터 형식 정규식 표현 : {차량종류}{폭}/{편평비}R{휠사이즈}

        for(String tireInfo : tires){
            if(tireInfo.matches(correctDataFormat)){    // 올바른 데이터 형식인 경우
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(tireInfo);

                // 폭, 편평비, 휠사이즈 값 추출
                matcher.find();
                int width           = Integer.parseInt(matcher.group());
                matcher.find();
                int aspectRatio     = Integer.parseInt(matcher.group());
                matcher.find();
                int wheelDiameter   = Integer.parseInt(matcher.group());

                Tire tire = tireRepository.findByWidthAndAspectRatioAndWheelDiameter(width, aspectRatio, wheelDiameter);
                if(tire == null){
                    tire = tireRepository.save(Tire.builder()
                                    .width(width)
                                    .aspectRatio(aspectRatio)
                                    .wheelDiameter(wheelDiameter)
                                    .build());
                }

                if(!userTireRepository.existsByUserAndTire(user, tire)){
                    UserTire userTire = UserTire.builder()
                            .user(user)
                            .tire(tire)
                            .build();
                    userTireList.add(userTire);
                }
            }
        }
    }
}

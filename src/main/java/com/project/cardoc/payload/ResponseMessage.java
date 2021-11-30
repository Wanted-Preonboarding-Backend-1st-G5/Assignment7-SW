package com.project.cardoc.payload;

public class ResponseMessage {
    private ResponseMessage(){}

    public static final String INTERNAL_SERVER_ERROR    = "서버 내부 에러";

    public static final String SUCCESS_USER_SIGNUP      = "유저 회원 가입 성공";
    public static final String SUCCESS_USER_LOGIN       = "유저 로그인 성공";

    public static final String FAIL_USER_SIGNUP_DUPLICATE_USER_ID = "중복된 ID 입니다";
    public static final String FAIL_USER_LOGIN_WRONG_PASSWORD = "잘못된 비밀번호 입니다.";

    public static final String SUCCESS_SAVE_TIRE_INFO   = "사용자가 소유한 타이어 정보 저장 성공";

    public static final String SUCCESS_GET_TIRE_LIST    = "사용자가 소유한 타이어 정보 조회 성공";

    public static final String FAIL_USERTIRE_NOT_VALID_NUMBER_OF_DATA_REQUESTS = "요청 값의 개수는 1개 이상 5개 이하여야 합니다.";
    public static final String FAIL_USERTIRE_CANNOT_FIND_CAR_INFO = " 해당 자동차 정보를 조회할 수 없습니다";
    public static final String FAIL_USERTIRE_CANNOT_FIND_TIRE_INFO = "해당 타이어 정보를 조회할 수 없습니다";

}

package com.project.cardoc.payload;

public class ResponseMessage {
    private ResponseMessage(){}

    public static final String INTERNAL_SERVER_ERROR    = "서버 내부 에러";

    public static final String SUCCESS_USER_SIGNUP      = "유저 회원 가입 성공";
    public static final String SUCCESS_USER_LOGIN       = "유저 로그인 성공";

    public static final String SUCCESS_SAVE_TIRE_INFO   = "사용자가 소유한 타이어 정보 저장 성공";
}

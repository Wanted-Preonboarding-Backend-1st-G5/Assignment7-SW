package com.project.cardoc.payload.response;

import lombok.*;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public final class UserResponse {

    private long    user_id;
    private String  accessToken;

    public static UserResponse of(long id, String accessToken){
        return UserResponse.builder()
                .user_id(id)
                .accessToken(accessToken)
                .build();
    }
}

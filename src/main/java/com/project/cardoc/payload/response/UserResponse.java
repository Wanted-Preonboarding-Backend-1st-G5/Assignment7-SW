package com.project.cardoc.payload.response;

import lombok.*;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public final class UserResponse {

  private long userId;
  private String accessToken;

  public static UserResponse of(final long id, final String accessToken) {
    return UserResponse.builder()
        .userId(id)
        .accessToken(accessToken)
        .build();
  }
}

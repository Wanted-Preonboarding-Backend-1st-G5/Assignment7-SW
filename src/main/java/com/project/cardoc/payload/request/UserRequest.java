package com.project.cardoc.payload.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class UserRequest {

  @NotNull
  private String id;

  @NotNull
  private String password;

}

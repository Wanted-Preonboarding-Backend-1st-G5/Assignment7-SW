package com.project.cardoc.payload;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultResponse<T> {

  public static final DefaultResponse<?> FAIL_DEFAULT_RES
      = of(ResponseCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
  private int status;
  private String message;
  private T response;

  public DefaultResponse(final int status, final String message) {
    this.status = status;
    this.message = message;
  }

  public DefaultResponse(final int status, final String message, final T response) {
    this.status = status;
    this.message = message;
    this.response = response;
  }

  public static DefaultResponse<?> of(@NotNull final int status, final String message) {
    return new DefaultResponse<>(status, message);
  }

  public static <T> DefaultResponse<T> of(@NotNull final int status, final String message, final T response) {
    return new DefaultResponse<>(status, message, response);
  }

}

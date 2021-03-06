package com.project.cardoc.payload;

public final class ResponseCode {

  private ResponseCode() {
  }

  public static final int INTERNAL_SERVER_ERROR = 500;
  public static final int OK = 200;
  public static final int AUTHENTICATION_ERROR = 401;
  public static final int RESOURCE_NOT_FOUND = 404;
  public static final int BAD_REQUEST = 400;
}

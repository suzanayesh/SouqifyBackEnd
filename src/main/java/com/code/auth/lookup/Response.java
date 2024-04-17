package com.code.auth.lookup;

import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
  public static ResponseEntity<ApiResponse<UserInfo>> createSuccessResponse(UserInfo userInfo) {
    return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<UserInfo>builder()
                    .status(true)
                    .username(userInfo.getName())  // Assuming getName() gives the correct username.
                    .storename(userInfo.getStorename())
                    .email(userInfo.getEmail())
                    .role(userInfo.getRole())
                    .data("Welcome " + userInfo.getName() + " to your profile")  // Custom message.
                    .statusCode(HttpStatus.OK.value())
                    .build());
  }




  public static <T> ResponseEntity<ApiResponse<T>> createFailResponse(String msg) {
    ApiResponse<T> apiResponse = ApiResponse.<T>builder()
            .status(false)
            .statusCode(HttpStatus.OK.value())
            .data(null)
            .build();

    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }
}

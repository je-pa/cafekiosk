package sample.cafekiosk.spring.api.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

  @ResponseStatus(BAD_REQUEST)// 해당 예외를 던질 때 어떤 status를 줄지 지정
  @ExceptionHandler(BindException.class)
  public ApiResponse<Object> bindException(BindException e) {
    return ApiResponse.of(
        BAD_REQUEST,
        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
        null
    );
  }

}
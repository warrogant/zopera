package com.example.zopera.exception;

import com.example.zopera.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     * 如果抛出的的是ServiceException，则调用该方法
     *
     * @param se
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handler(ServiceException se) {
        //MyBaseException自定义异常
        return Result.error(se.getCode(), se.getMessage());
    }

}

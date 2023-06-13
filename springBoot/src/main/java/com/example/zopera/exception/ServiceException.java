package com.example.zopera.exception;

import lombok.Data;

/*
RuntimeException: 运行异常
 */
@Data
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}

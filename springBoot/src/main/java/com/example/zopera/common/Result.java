package com.example.zopera.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
接口统一返回包装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    //    默认
    public static Result success() {
        return new Result(ResultConstants.CODE_200, "", null);
    }

    //  返回成功代码以及数据
    public static Result success(Object data) {
        return new Result(ResultConstants.CODE_200, "", data);
    }

    //  返回错误代码以及错误信息
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    //    默认
    public static Result error() {
        return new Result(ResultConstants.CODE_500, "系统错误", null);
    }

}

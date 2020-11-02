package com.gr.security.sso.common;

import com.gr.security.sso.enums.ResultCodeEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回类型
 * @author lfz
 * @since 2020.09.30
 */
@Data
public class ResultJson<T> implements Serializable{

    private static final long serialVersionUID = 783015033603078674L;
    private int code;
    private String msg;
    private T data;

    public static ResultJson ok() {
        return ok("");
    }

    public static ResultJson ok(Object o) {
        return new ResultJson(ResultCodeEnums.SUCCESS, o);
    }

    public static ResultJson failure(ResultCodeEnums code) {
        return failure(code, "");
    }

    public static ResultJson failure(ResultCodeEnums code, Object o) {
        return new ResultJson(code, o);
    }

    public ResultJson (ResultCodeEnums resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson (ResultCodeEnums resultCode,T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCodeEnums resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}

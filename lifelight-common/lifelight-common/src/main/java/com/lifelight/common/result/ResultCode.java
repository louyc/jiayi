package com.lifelight.common.result;

import java.io.Serializable;

public class ResultCode implements Serializable {

    private static final long serialVersionUID = 9096720431761678363L;
    private String code;
    private String message;

    public ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultCode{code=" + code + ", message='" + message + "'} ";
    }
}

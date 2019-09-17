package com.lifelight.common.result;

import java.io.Serializable;
import java.util.List;

public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 319880087162489843L;
    
    protected int status;
    protected boolean isSuccessful;
    protected ResultCode resultCode;
    protected String token;
    protected T data;
    
    public Result(int status) {
        this.status = status;
    }

    public Result(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public Result(int status, boolean isSuccessful) {
        this.status = status;
        this.isSuccessful = isSuccessful;
    }

    public Result(int status, boolean isSuccessful, ResultCode resultCode) {
        this.status = status;
        this.isSuccessful = isSuccessful;
        this.resultCode = resultCode;
    }
    
    public Result(int status, boolean isSuccessful, ResultCode resultCode, T data) {
        this.status = status;
        this.isSuccessful = isSuccessful;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

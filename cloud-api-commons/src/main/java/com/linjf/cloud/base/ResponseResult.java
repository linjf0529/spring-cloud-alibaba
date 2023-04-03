package com.linjf.cloud.base;

import lombok.Data;

/**
 * @author linjf
 * @create 2022/2/17 11:18
 */
@Data
public class ResponseResult<T> {
    //响应编码
    private Integer code;
    //响应描述
    private String message;
    //响应对象
    private T result;

    public ResponseResult(Integer code) {
        this.code = code;
    }

    public ResponseResult() {
        this.code = 200;
        this.message = "操作成功";
    }

    public ResponseResult(T result) {
        this.code = 200;
        this.message = "操作成功";
        this.result = result;
    }

    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult(data);
    }

    public static <T> ResponseResult<T> ok() {
        return new ResponseResult();
    }

    public static <T> ResponseResult<T> fail(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(500);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static <T> ResponseResult<T> fail() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(500);
        responseResult.setMessage("操作失败");
        return responseResult;
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static <T> ResponseResult<T> isSuccess(long count) {
        return count > 0L ? ok() : fail();
    }

    public static <T> ResponseResult<T> isSuccess(int count) {
        return count > 0 ? ok() : fail();
    }

    public static <T> ResponseResult<T> getResponse(Integer code) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        return responseResult;
    }


    public T getResult() {
        return this.result;
    }

    public ResponseResult setData(T result) {
        this.result = result;
        return this;
    }


    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public String getMessage() {
        return this.message;
    }

    public ResponseResult setMessage(String message) {
        this.message = message;
        return this;
    }
}

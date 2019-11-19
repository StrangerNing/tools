package me.znzn.tools.common.component;

import java.io.Serializable;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/4/2
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -2310002475443677643L;
    private static final Integer STATUS_SUCCESS = 200;
    private static final Integer STATUS_FAILURE = -1;
    private static final Integer STATUS_ERROR = 500;
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAILURE = "failure";
    public static final String MSG_ERROR = "error";

    /**
     * 正常返回
     */
    public static final String CODE_SUCCESS = "1";
    /**
     * 状态改变
     */
    public static final String CODE_STATUS_ERROR = "2";
    /**
     * 数据版本异常
     */
    public static final String CODE_VERSION_ERROR = "3";
    /**
     * 参数校验异常
     */
    public static final String CODE_PARAM_ERROR = "4";
    /**
     * 其他
     */
    public static final String CODE_OTHER = "99";


    /**
     * 状态
     */
    private int status;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public Result(){

    }

    public Result(int status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public Result(int status, boolean success, String code, String message) {
        this.status = status;
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Result(int status, boolean success, String message, T data) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(int status, boolean success, String code, String message, T data) {
        this.status = status;
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求成功
     *
     * @param
     * @return
     */
    public static Result success() {
        return new Result(STATUS_SUCCESS, Boolean.TRUE, Result.CODE_SUCCESS, MSG_SUCCESS);
    }

    /**
     * 请求成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(STATUS_SUCCESS, Boolean.TRUE, Result.CODE_SUCCESS, MSG_SUCCESS, data);
    }

    /**
     * 请求成功
     * @param message
     * @param code
     * @return
     */
    public static Result success(String message, String code){
        return new Result(STATUS_SUCCESS, Boolean.TRUE, code, message);
    }

    /**
     * 失败
     *
     * @param
     * @return
     */
    public static Result failure() {
        return new Result(STATUS_FAILURE, Boolean.FALSE, Result.CODE_OTHER, MSG_FAILURE);
    }

    /**
     * 失败，带消息
     *
     * @param message
     * @param
     * @return
     */
    public static Result failure(String message) {
        return new Result(STATUS_FAILURE, Boolean.FALSE, Result.CODE_OTHER, message);
    }

    /**
     * 失败，带消息，带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(String message, T data) {
        return new Result<T>(STATUS_FAILURE, Boolean.FALSE, Result.CODE_OTHER, message, data);
    }

    /**
     * 失败，带消息，带错误码
     *
     * @param message
     * @param code
     * @return
     */
    public static <T> Result<T> failure(String message, String code) {
        return new Result<T>(STATUS_FAILURE, Boolean.FALSE, code, message);
    }

    /**
     * 失败，带消息，带数据，带错误码
     *
     * @param data
     * @param <T>
     * @param code
     * @return
     */
    public static <T> Result<T> failure(String message, T data, String code) {
        return new Result<T>(STATUS_FAILURE, Boolean.FALSE, code, message, data);
    }

    /**
     * 错误
     *
     * @param
     * @return
     */
    public static Result error() {
        return new Result(STATUS_ERROR, Boolean.FALSE, Result.CODE_OTHER, MSG_ERROR);
    }

    /**
     * 错误，带消息
     *
     * @param message
     * @return
     */
    public static Result error(String message) {
        return new Result(STATUS_ERROR, Boolean.FALSE, Result.CODE_OTHER, message);
    }

    /**
     * 错误，带消息 ,带code
     *
     * @param message
     * @return
     */
    public static Result error(String message, String code) {
        return new Result(STATUS_ERROR, Boolean.FALSE, code, message);
    }

    /**
     * 错误，带消息，带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String message, T data) {
        return new Result<T>(STATUS_ERROR, Boolean.FALSE, Result.CODE_OTHER,message, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", success=" + success +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

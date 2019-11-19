package me.znzn.tools.common.enums;

public enum ResultEnum {
    /**
     * 成功. ErrorCode : 0
     */
    SUCCESS("0", "成功"),
    /**
     * 未知异常. ErrorCode : 01
     */
    UnknownException("01", "未知异常"),
    /**
     * 系统异常. ErrorCode : 02
     */
    SystemException("02", "系统异常"),
    /**
     * 业务错误. ErrorCode : 03
     */
    MyException("03", "业务错误"),
    /**
     * 提示级错误. ErrorCode : 04
     */
    InfoException("04", "提示级错误"),
    /**
     * 数据库操作异常. ErrorCode : 020001
     */
    DBException("020001", "数据库操作异常"),
    /**
     * 参数验证错误. ErrorCode : 040001
     */
    ParamException("040001", "参数验证错误");

    private String code;

    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

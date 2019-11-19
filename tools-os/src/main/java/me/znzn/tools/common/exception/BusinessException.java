package me.znzn.tools.common.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/4/2
 */
public class BusinessException extends BizException {

    public BusinessException(String msg) {
        super(msg);
        super.setTextMessage(msg);
    }

    public BusinessException(IErrorEnums errorEnums) {
        super(errorEnums.getCode(), errorEnums.getMsg());
        super.setTextMessage(errorEnums.getMsg());
    }

    public BusinessException(String msg, Throwable cause) {
        super(null, msg, cause);
        super.setTextMessage(msg);
    }

    public BusinessException(String code, String msg) {
        super(code, msg);
        super.setTextMessage(msg);
    }

    public BusinessException(IErrorEnums errorEnums, Throwable cause) {
        super(errorEnums.getCode(), errorEnums.getMsg(), cause);
        super.setTextMessage(errorEnums.getMsg());
    }

    public BusinessException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
        super.setTextMessage(msg);
    }

    public BusinessException(IErrorEnums errorEnums, Object... args) {
        super(errorEnums.getCode(), args, errorEnums.getMsg());
        super.setTextMessage(errorEnums.getMsg());
    }

    public BusinessException(String code, String msg, Object[] args) {
        super(code, args, msg);
        super.setTextMessage(msg);
    }

    public BusinessException(IErrorEnums errorEnums, Throwable cause, Object[] args) {
        super(errorEnums.getCode(), args, errorEnums.getMsg(), cause);
        super.setTextMessage(errorEnums.getMsg());
    }

    public BusinessException(String code, String msg, Throwable cause, Object[] args) {
        super(code, args, msg, cause);
        super.setTextMessage(msg);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getTextMessage() {
        Object[] args = super.getArgs();
        String textMessage = super.getTextMessage();
        if (args == null || 0 == args.length){
            return textMessage;
        }
        return MessageFormatter.arrayFormat(textMessage, args).getMessage();
    }
}

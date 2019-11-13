package me.znzn.tools.common.exception;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/4/2
 */
public class BizException extends BusinessRuntimeException {


    private static final long serialVersionUID = 2010724078126788258L;
    /**
     * 应用异常码
     */
    private String code;

    /**
     * 应用异常描述参数
     */
    private Object[] args;

    /**
     * 文本消息
     */
    private String textMessage;

    public BizException(String msg){
        super(msg);
    }

    public BizException(String code, String msg) {
        super(code + ": " + msg);
        this.code = code;
    }

    public BizException(String code, String msg, Throwable cause) {
        super(code + ": " + msg, cause);
        this.code = code;
    }

    public BizException(String code, Object[] args, String msg) {
        super(code + ": " + msg);
        this.code = code;
        this.args = args;
    }

    public BizException(String code, Object[] args, String msg, Throwable cause) {
        super(code + ": " + msg, cause);
        this.code = code;
        this.args = args;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String string) {
        code = string;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] objects) {
        args = objects;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}

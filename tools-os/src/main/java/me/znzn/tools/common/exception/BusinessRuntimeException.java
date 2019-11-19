package me.znzn.tools.common.exception;

/**
 * @author zening.zhu
 * @version 1.0
 * @date 2019/4/2
 */
public class BusinessRuntimeException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -7464087034363911027L;

    public BusinessRuntimeException(){
        super();
    }

    public BusinessRuntimeException(String message){
        super(message);
    }

    public BusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuntimeException(Throwable cause) {
        super(cause);
    }
}

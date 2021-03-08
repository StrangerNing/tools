package me.znzn.tools.common.exception;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/3/6
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(){
        super();
    }

    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}

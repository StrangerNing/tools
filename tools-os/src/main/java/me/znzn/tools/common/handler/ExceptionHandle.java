package me.znzn.tools.common.handler;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zening.zhu
 * @version v1.0
 * @date 2019/4/10
 */

@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity handle(Exception e){
        if (e instanceof BusinessException){
            BusinessException businessException = (BusinessException)e;
            return ResultPageUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, businessException.getTextMessage(), Integer.valueOf(businessException.getCode()));
        }else {
            logger.error("系统异常，{}",e);
            return ResultPageUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "系统异常");
        }
    }
}

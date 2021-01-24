package me.znzn.tools.common.component;

import me.znzn.tools.common.enums.ExceptionEnum;
import me.znzn.tools.common.enums.SuccessEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 与前端交互的工具类
 */
public class ResultPageUtil {

    /**
     * 成功返回有值
     * @param object 返回数据
     * @return
     */
    public static <T> ResponseEntity<T> success(T object) {
        return success(object, null);
    }

    /**
     * 成功返回有值 分页
     * @param object 返回数据
     * @return
     */
    public static ResponseEntity<ResultListData> successWithPage(Object object, Integer totalCount, Integer currentPage) {
        ResultListData resultData = new ResultListData();
        resultData.setList(object);
        resultData.setTotalCount(totalCount);
        resultData.setCurrentPage(currentPage);
        return success(resultData,null);
    }

    /**
     * 无值成功返回
     * @return
     */
    public static ResponseEntity success(){
        return success(null);
    }

    /**
     * 成功返回有值,并自定义msg信息
     * @param object 返回数据
     * @param customMessage 自定义信息
     * @return
     */
    public static <T> ResponseEntity<T> success(T object, String customMessage) {
        ResultMsgData<T> resultData = new ResultMsgData();
        if (customMessage != null && StringUtils.isNotEmpty(customMessage)) {
            resultData.setMsg(customMessage);
        } else {
            resultData.setMsg(SuccessEnum.CommonSuccess.OPERATE.getName());
        }
        resultData.setData(object);
        resultData.setStatus(1);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }


    /**
     * 默认错误返回
     * @return
     */
    public static ResponseEntity error(){
        return error(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 自定义错误返回
     * @param httpStatusEnum 错误类型
     * @return
     */
    public static ResponseEntity error(HttpStatus httpStatusEnum){
        return error(httpStatusEnum, null);
    }

    /**
     * 自定义错误返回信息
     * @return
     */
    public static ResponseEntity error(String customMessage){
        return error(HttpStatus.INTERNAL_SERVER_ERROR, customMessage);
    }

    /**
     * 自定义错误类型和错误信息
     *
     * @param customMessage  自定义错误信息
     * @return
     */
    public static ResponseEntity error(HttpStatus httpStatus, String customMessage) {

        ResultMsgData resultVo = new ResultMsgData();
        if (customMessage != null && !customMessage.isEmpty()) {
            resultVo.setMsg(customMessage);
        } else {
            //此版本的httpstatus类不支持扩展 所以先预留 使用默认的中文
            resultVo.setMsg(ExceptionEnum.CommonExpetion.SYSTEM.getName());
        }
        resultVo.setStatus(0);
        ResponseEntity responseEntity = new ResponseEntity(resultVo, httpStatus);
        return responseEntity;
    }

    public static ResponseEntity error(HttpStatus httpStatus, String customMessage, Integer status) {
        ResultMsgData resultVo = new ResultMsgData();
        if (customMessage != null && !customMessage.isEmpty()) {
            resultVo.setMsg(customMessage);
        } else {
            //此版本的httpstatus类不支持扩展 所以先预留 使用默认的中文
            resultVo.setMsg(ExceptionEnum.CommonExpetion.SYSTEM.getName());
        }
        resultVo.setStatus(status);
        ResponseEntity responseEntity = new ResponseEntity(resultVo, httpStatus);
        return responseEntity;
    }
}

package me.znzn.tools.common.component;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回前端的消息对象类
 */
@Data
public class ResultMsgData<T> implements Serializable{

    /** 是否成功 .*/
    private int status;

    /** 消息 .*/
    private String msg;

    /** 具体值 .*/
    private T data;


}

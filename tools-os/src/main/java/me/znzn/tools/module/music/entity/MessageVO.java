package me.znzn.tools.module.music.entity;

import lombok.Data;
import me.znzn.tools.utils.JsonUtils;

import java.io.Serializable;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/16
 */
@Data
public class MessageVO implements Serializable {

    private static final long serialVersionUID = -2781029204827630043L;

    private String type;

    private String data;

    public MessageVO() {
        type = null;
        data = null;
    }

    public MessageVO(String data) {
        this.type = "msg";
        this.data = data;
    }

    public MessageVO(String type, String data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

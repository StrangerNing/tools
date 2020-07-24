package me.znzn.tools.module.music.entity;

import lombok.Data;
import me.znzn.tools.utils.JsonUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/22
 */
@Data
public class ChatVO implements Serializable {

    private static final long serialVersionUID = -5594650126953521331L;

    private String type;

    private String data;

    private String user;

    private String time;

    public ChatVO(String data, String user) {
        this.type = "chat";
        this.user = user;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.time = simpleDateFormat.format(new Date());
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

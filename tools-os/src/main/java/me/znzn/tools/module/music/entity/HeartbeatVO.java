package me.znzn.tools.module.music.entity;

import lombok.Data;
import me.znzn.tools.utils.JsonUtils;

import java.io.Serializable;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Data
public class HeartbeatVO implements Serializable {

    private static final long serialVersionUID = -7510043547297681040L;

    private String type;

    private String data;

    public HeartbeatVO(int count) {
        this.type = "online";
        this.data = String.valueOf(count);
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

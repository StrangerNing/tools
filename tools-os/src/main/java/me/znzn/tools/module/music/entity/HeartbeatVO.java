package me.znzn.tools.module.music.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.znzn.tools.utils.JsonUtils;

import java.io.Serializable;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HeartbeatVO extends MessageVO implements Serializable {

    private static final long serialVersionUID = -7510043547297681040L;

    public HeartbeatVO(int count) {
        this.setType("online");
        this.setData(String.valueOf(count));
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

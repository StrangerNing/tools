package me.znzn.tools.module.music.entity;

import lombok.Data;
import me.znzn.tools.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/23
 */
@Data
public class MusicPushVO implements Serializable {

    private static final long serialVersionUID = -3461645000025233690L;

    private String type;

    private Long id;

    private String name;

    private String file;

    private String album;

    private List<String> artists;

    private String image;

    private String lrcs;

    private String user;

    private String current;

    private Long length;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

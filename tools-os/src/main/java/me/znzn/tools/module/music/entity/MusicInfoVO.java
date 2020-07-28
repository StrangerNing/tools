package me.znzn.tools.module.music.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/23
 */
@Data
public class MusicInfoVO implements Serializable {

    private static final long serialVersionUID = -7260543176174384147L;

    private String id;

    private String name;

    private List<String> artist;

    private String album;

    private String pic_id;

    private String url_id;

    private String lyric_id;

    private String source;
}

package me.znzn.tools.module.music.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/23
 */
@Data
public class MusicUrlVO implements Serializable {

    private static final long serialVersionUID = 8559416062728889701L;

    private String url;

    private Long size;

    private Integer br;
}

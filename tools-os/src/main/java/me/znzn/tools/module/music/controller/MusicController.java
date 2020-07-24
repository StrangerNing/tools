package me.znzn.tools.module.music.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/16
 */
@Controller
public class MusicController {

    @RequestMapping("/music")
    public String index() {
        return "music_together.html";
    }
}

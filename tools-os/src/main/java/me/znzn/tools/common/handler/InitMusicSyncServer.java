package me.znzn.tools.common.handler;

import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.websocket.MusicControlService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/27
 */
@Slf4j
@Component
@Order(value = 3)
public class InitMusicSyncServer implements ApplicationRunner {

    @Resource
    private MusicControlService musicControlService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动音乐服务……");
        musicControlService.init();
    }
}

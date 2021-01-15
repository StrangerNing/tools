package me.znzn.tools.module.music.processor.impl;

import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.module.music.websocket.MusicControlService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Component
public class MsgProcessor implements MessageProcessor {

    @Resource
    private MusicControlService musicControlService;

    @Override
    public void execute(WebSocketSession session, WebSocketMessage message) {

    }
}

package me.znzn.tools.module.music.processor.impl;

import me.znzn.tools.module.music.processor.MessageProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Component
public class MsgProcessor implements MessageProcessor {

    @Override
    public void excute(WebSocketSession session, WebSocketMessage message) {

    }
}

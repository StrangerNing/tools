package me.znzn.tools.module.music.processor;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
public interface MessageProcessor {

    /**
     * 处理信息
     * @param session websocket session
     * @param message received message
     */
    void excute(WebSocketSession session, WebSocketMessage message) throws IOException;
}

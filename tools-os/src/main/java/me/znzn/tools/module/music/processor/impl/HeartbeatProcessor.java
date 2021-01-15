package me.znzn.tools.module.music.processor.impl;

import me.znzn.tools.module.music.entity.HeartbeatVO;
import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.module.music.websocket.WsSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Component
public class HeartbeatProcessor implements MessageProcessor {

    @Override
    public void execute(WebSocketSession session, WebSocketMessage message) throws IOException {
        WsSessionManager.sendMessage(session, new HeartbeatVO(WsSessionManager.SESSION_POOL.size()));
    }
}

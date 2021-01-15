package me.znzn.tools.module.music.processor.impl;

import me.znzn.tools.module.music.entity.MessageVO;
import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.module.music.websocket.MusicControlService;
import me.znzn.tools.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/30
 */
@Component
public class DurationProcessor implements MessageProcessor {

    @Resource
    private MusicControlService musicControlService;

    @Override
    public void execute(WebSocketSession session, WebSocketMessage message) throws IOException {
        MessageVO text = JsonUtils.toObject(((TextMessage) message).asBytes(), MessageVO.class);
        double duration = Double.parseDouble(text.getData());
        musicControlService.setMusicDuration(duration);
    }
}

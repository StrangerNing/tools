package me.znzn.tools.module.music.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.MessageVO;
import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.utils.JsonUtils;
import me.znzn.tools.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/16
 */
@Slf4j
@Component
public class MusicWebSocketServer extends TextWebSocketHandler {

    @Resource
    private MusicControlService musicControlService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = WsSessionManager.getToken(session);
        WsSessionManager.add(token, session);
        MessageVO message = new MessageVO("Join chat room successfully");
        TextMessage textMessage = new TextMessage(JsonUtils.toJson(message));
        session.sendMessage(textMessage);
        musicControlService.sendNowMusic(session);
        musicControlService.sendMusicList(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            String type = (String)JSON.parseObject((String) message.getPayload()).get("type");
            try {
                MessageProcessor messageProcessor = (MessageProcessor)SpringUtil.getBean(type + "Processor");
                messageProcessor.excute(session, message);
            } catch (BeansException e) {
                log.error("查找处理bean失败", e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = WsSessionManager.getToken(session);
        WsSessionManager.remove(token);
    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }
}

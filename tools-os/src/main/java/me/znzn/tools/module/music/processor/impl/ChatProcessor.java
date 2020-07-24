package me.znzn.tools.module.music.processor.impl;

import me.znzn.tools.module.music.entity.*;
import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.module.music.websocket.WsSessionManager;
import me.znzn.tools.utils.JsonUtils;
import me.znzn.tools.utils.MusicUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Component
public class ChatProcessor implements MessageProcessor {

    @Resource
    private RedisTemplate<String, MusicPushVO> redisTemplate;

    @Override
    public void excute(WebSocketSession session, WebSocketMessage message) throws IOException {
        MessageVO text = JsonUtils.toObject(((TextMessage) message).asBytes(), MessageVO.class);
        if (WsSessionManager.getUserNickName(session) == null && !text.getData().startsWith("设置昵称")) {
            session.sendMessage(new TextMessage(new MessageVO("Please set nickname first").toString()));
            return;
        }
        if (text.getData().startsWith("设置昵称 ")) {
            String nickName = text.getData().substring(5);
            WsSessionManager.setUserNickname(session, nickName);
            session.sendMessage(new TextMessage(new MessageVO("setname",nickName).toString()));
            session.sendMessage(new TextMessage(new MessageVO("Set nickname successfully").toString()));
        } else if (text.getData().startsWith("点歌 ")) {
            String name = text.getData().substring(3);
            List<MusicInfoVO> musicList = MusicUtil.searchToVO(name);
            if (CollectionUtils.isEmpty(musicList)) {
                session.sendMessage(new TextMessage(new MessageVO("没有找到相关歌曲").toString()));
            } else {
                MusicInfoVO music = musicList.get(0);
                MusicUrlVO musicUrl = MusicUtil.getMusicUrl(music.getId());
                if (musicUrl.getSize() == 0L) {
                    session.sendMessage(new TextMessage(new MessageVO("歌曲无效").toString()));
                    return;
                }
                MusicPushVO musicPushVO = new MusicPushVO();
                musicPushVO.setType("music");
                musicPushVO.setId(music.getId());
                musicPushVO.setName(music.getName());
                musicPushVO.setAlbum(music.getAlbum());
                musicPushVO.setArtists(music.getArtist());
                musicPushVO.setFile(musicUrl.getUrl());
                musicPushVO.setImage(MusicUtil.getMusicImage(music.getPic_id()));
                musicPushVO.setCurrent("0");
                musicPushVO.setLrcs(MusicUtil.getMusicLrcs(music.getId()));
                musicPushVO.setUser(WsSessionManager.getUserNickName(session));
                redisTemplate.opsForList().rightPush("music_list", musicPushVO);
                session.sendMessage(new TextMessage(musicPushVO.toString()));
                String msg = "用户 " + WsSessionManager.getUserNickName(session) + " 点歌 " + music.getName() + "-" + music.getArtist().toString();
                session.sendMessage(new TextMessage(new MessageVO(msg).toString()));
            }
        } else {
            WsSessionManager.SESSION_POOL.values().forEach(item -> {
                try {
                    item.sendMessage(new TextMessage(new ChatVO(text.getData(), WsSessionManager.getUserNickName(session)).toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

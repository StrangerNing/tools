package me.znzn.tools.module.music.processor.impl;

import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.*;
import me.znzn.tools.module.music.processor.MessageProcessor;
import me.znzn.tools.module.music.websocket.MusicControlService;
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
import java.util.Random;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/21
 */
@Slf4j
@Component
public class ChatProcessor implements MessageProcessor {

    @Resource
    private RedisTemplate<String, MusicPushVO> redisTemplate;

    @Resource
    private MusicControlService musicControlService;

    @Override
    public void excute(WebSocketSession session, WebSocketMessage message) throws IOException {
        MessageVO text = JsonUtils.toObject(((TextMessage) message).asBytes(), MessageVO.class);
        if (WsSessionManager.getUserNickName(session) == null && !text.getData().startsWith("设置昵称")) {
            String nickname = "用户" + new Random().nextInt(10000);
            WsSessionManager.setUserNickname(session, nickname);
            musicControlService.sendMessage(session, new MessageVO("setname", nickname));
        }
        if (text.getData().startsWith("设置昵称 ")) {
            String nickName = text.getData().substring(5);
            WsSessionManager.setUserNickname(session, nickName);
            musicControlService.sendMessage(session, new MessageVO("setname", nickName));
            musicControlService.sendMessage(session, new MessageVO("Set nickname successfully"));
        } else if (text.getData().startsWith("点歌 ")) {
            String name = text.getData().substring(3);
            getMusic(session, name, "netease");
        } else if (text.getData().startsWith("酷狗点歌 ")) {
            String name = text.getData().substring(5);
            getMusic(session, name, "kugou");
        } else if (text.getData().startsWith("投票切歌")) {
            musicControlService.broadcastNextMusic();
            musicControlService.sendMessage(session, new MessageVO("对不起，这个功能还没开发hhh，但我先帮你把这首歌切掉了"));
            musicControlService.broadcastMessage(new MessageVO("用户 " + WsSessionManager.getUserNickName(session) + " 切掉了这首歌"));
        } else {
            WsSessionManager.broadcast(new ChatVO(text.getData(), WsSessionManager.getUserNickName(session)));
        }
    }

    private void getMusic(WebSocketSession session, String name, String source) {
        List<MusicInfoVO> musicList = MusicUtil.searchBySourceToVO(name, source);
        if (CollectionUtils.isEmpty(musicList)) {
            musicControlService.sendMessage(session, new MessageVO("没有找到相关歌曲"));
        } else {
            MusicInfoVO music = musicList.get(0);
            MusicPushVO musicPushVO = MusicUtil.getMusic(music);
            if (musicPushVO.getFile() == null) {
                musicControlService.sendMessage(session, new MessageVO("歌曲无效"));
                return;
            }
            musicPushVO.setUser(WsSessionManager.getUserNickName(session));
            musicControlService.saveSongId(music);
            redisTemplate.opsForList().rightPush("music_list", musicPushVO);
            String msg = "用户 " + WsSessionManager.getUserNickName(session) + " 点歌 " + music.getName() + "-" + music.getArtist().toString();
            WsSessionManager.broadcast(new MessageVO(msg));
            musicControlService.broadcastMusicList();
        }
    }
}

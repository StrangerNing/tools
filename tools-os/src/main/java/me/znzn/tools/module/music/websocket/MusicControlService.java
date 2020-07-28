package me.znzn.tools.module.music.websocket;

import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.MessageVO;
import me.znzn.tools.module.music.entity.MusicPushVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/27
 */
@Slf4j
@Component
public class MusicControlService {

    @Resource
    private RedisTemplate redisTemplate;

    private static String MUSIC_LIST_KEY = "music_list";

    private static String SYNC_KEY = "music_sync_key";

    private static MusicPushVO music;

    public void init() {
        while (true) {
            if (WsSessionManager.SESSION_POOL.size() == 0) {
                continue;
            }
            Long musicListSize = redisTemplate.opsForList().size(MUSIC_LIST_KEY);
            if (musicListSize != null && musicListSize > 0) {
                if (music == null) {
                    broadcastNextMusic();
                } else {
                    Long time = (Long)redisTemplate.opsForValue().get(SYNC_KEY);
                    if (time != null && music.getLength() != null) {
                        if (System.currentTimeMillis() - time > music.getLength() + 2000) {
                            broadcastNextMusic();
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException i) {
                log.error("线程切换失败", i);
            }
        }
    }

    private void broadcastNewMusic() {
        music = (MusicPushVO)redisTemplate.opsForList().leftPop(MUSIC_LIST_KEY);
        redisTemplate.opsForValue().set(SYNC_KEY, System.currentTimeMillis());
        WsSessionManager.broadcast(music);
    }

    public void broadcastNextMusic() {
        broadcastNewMusic();
        broadcastMusicList();
    }

    public void sendNowMusic(WebSocketSession session) {
        if (music != null) {
            Long current = (Long)redisTemplate.opsForValue().get(SYNC_KEY);
            if (current != null) {
                long length = (System.currentTimeMillis() - current);
                MusicPushVO musicPushVO = new MusicPushVO();
                BeanUtils.copyProperties(music, musicPushVO);
                musicPushVO.setCurrent(String.valueOf((double)length / 1000));
                try {
                    session.sendMessage(new TextMessage(musicPushVO.toString()));
                } catch (IOException i) {
                    log.error("IO异常", i);
                }
            }
        }
    }

    public void sendMusicList(WebSocketSession session) {
        String html = getMusicList();
        if (html != null) {
            try {
                session.sendMessage(new TextMessage(new MessageVO("list", html).toString()));
            } catch (IOException i) {
                log.error("IO异常", i);
            }
        }
    }

    public void broadcastMusicList() {
        String html = getMusicList();
        WsSessionManager.broadcast(new MessageVO("list", html));
    }

    private String getMusicList() {
        List<MusicPushVO> musicList = redisTemplate.opsForList().range(MUSIC_LIST_KEY, 0, -1);
        if (!CollectionUtils.isEmpty(musicList)) {
            StringBuilder html = new StringBuilder("<tr>\n" +
                    "\t<th>ID</th>\n" +
                    "\t<th>歌名</th>\n" +
                    "\t<th>歌手</th>\n" +
                    "\t<th>专辑</th>\n" +
                    "\t<th>点歌");
            for (int i = 0; i < musicList.size(); i++) {
                MusicPushVO music = musicList.get(i);
                html.append("<tr> <td>")
                        .append(i + 1)
                        .append("</td><td>")
                        .append(music.getName())
                        .append("</td><td>")
                        .append(music.getArtists().toString())
                        .append("</td><td>")
                        .append(music.getAlbum())
                        .append("</td><td>")
                        .append(music.getUser())
                        .append("</td>");
            }
            return html.toString();
        }
        return null;
    }

    public void sendMessage(WebSocketSession session, MessageVO messageVO) {
        try {
            session.sendMessage(new TextMessage(messageVO.toString()));
        } catch (IOException i) {
            log.error("IO异常", i);
        }
    }

    public void broadcastMessage(MessageVO messageVO) {
        WsSessionManager.broadcast(messageVO);
    }
}

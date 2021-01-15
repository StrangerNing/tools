package me.znzn.tools.module.music.websocket;

import io.netty.util.internal.MathUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.MessageVO;
import me.znzn.tools.module.music.entity.MusicInfoVO;
import me.znzn.tools.module.music.entity.MusicPushVO;
import me.znzn.tools.utils.MusicUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
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

    private static final String MUSIC_LIST_KEY = "music_list";

    private static final String SYNC_KEY = "music_sync_key";

    private static final String RANDOM_KEY = "music_random";

    private static final String CUT_SONG_KEY = "music_cut";

    private static MusicPushVO music;

    @Async
    public void init() {
        while (WsSessionManager.SESSION_POOL.size() != 0) {
            Long musicListSize = redisTemplate.opsForList().size(MUSIC_LIST_KEY);
            if (music != null && (music.getLength() == null || music.getLength() == 0 || music.getLength().isNaN())) {
                WsSessionManager.broadcast(new MessageVO("duration" , "true"));
            }
            if (musicListSize != null && musicListSize > 0) {
                if (music == null || music.getIsRandom()) {
                    broadcastNextMusic();
                } else {
                    Long time = (Long) redisTemplate.opsForValue().get(SYNC_KEY);
                    if (time != null && music.getLength() != null) {
                        if (System.currentTimeMillis() - time > (music.getLength() * 1000 + 2000)) {
                            broadcastNextMusic();
                        }
                    }
                }
            } else {
                Long time = (Long) redisTemplate.opsForValue().get(SYNC_KEY);
                if (music == null) {
                    sendRandomSong();
                } else {
                    if (time != null && music.getLength() != null && !music.getLength().isNaN()) {
                        if (System.currentTimeMillis() - time > (music.getLength() * 1000 + 2000)) {
                            sendRandomSong();
                        }
                    }
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException i) {
                log.error("线程切换失败", i);
            }
        }
        log.error("无人在线，线程停止");
    }

    private void broadcastNewMusic() {
        music = (MusicPushVO)redisTemplate.opsForList().leftPop(MUSIC_LIST_KEY);
        if (music == null) {
            sendRandomSong();
        } else {
            redisTemplate.opsForValue().set(SYNC_KEY, System.currentTimeMillis());
            WsSessionManager.broadcast(music);
            cleanCutSongVoted();
        }
    }

    public void broadcastNextMusic() {
        broadcastNewMusic();
        broadcastMusicList();
    }

    public void sendNowMusic(WebSocketSession session) {
        if (music != null) {
            sendVoteNum(session);
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

    public void saveSongId(MusicInfoVO musicInfoVO) {
        redisTemplate.opsForSet().add(RANDOM_KEY, musicInfoVO);
    }

    /**
     * 推送播放新的随机歌曲
     */
    public void sendRandomSong() {
        MusicInfoVO song = (MusicInfoVO)redisTemplate.opsForSet().randomMember(RANDOM_KEY);
        if (song == null) {
            return;
        }
        redisTemplate.opsForValue().set(SYNC_KEY, System.currentTimeMillis());
        MusicPushVO musicPushVO = MusicUtil.getMusic(song);
        musicPushVO.setIsRandom(true);
        music = musicPushVO;
        WsSessionManager.broadcast(musicPushVO);
        WsSessionManager.broadcast(new MessageVO("系统随机播放：" + music.getName()));
        cleanCutSongVoted();
    }

    public String isRandomMusic() {
        return music != null && music.getIsRandom() ? music.getName() : null;
    }

    public void setMusicDuration(double duration) {
        if (music != null && (music.getLength() == null || music.getLength() == 0 || music.getLength().isNaN())) {
            music.setLength(duration);
        }
    }

    public void cutSong(WebSocketSession session) {
        BigDecimal cutPercent = new BigDecimal(0.3);
        String token = WsSessionManager.getToken(session);
        Boolean isVoted = redisTemplate.opsForSet().isMember(CUT_SONG_KEY, token);
        if (isVoted == null) {
            return;
        }
        if (isVoted) {
            WsSessionManager.sendMessage(session, new MessageVO("你已经投过票了"));
        }
        redisTemplate.opsForSet().add(CUT_SONG_KEY, token);
        BigDecimal votedSize = getVotedSize();
        BigDecimal onlineSize = new BigDecimal(WsSessionManager.SESSION_POOL.size());

        if (votedSize != null && onlineSize != null) {
            boolean canCut = votedSize.compareTo(onlineSize.multiply(cutPercent)) > -1;
            if (canCut) {
                broadcastNewMusic();
                WsSessionManager.broadcast(new MessageVO("切歌成功"));
            }
        }
        sendVoteNum();
    }

    public void cleanCutSongVoted() {
        redisTemplate.delete(CUT_SONG_KEY);
        WsSessionManager.broadcast(new MessageVO("cut", "0"));
    }

    public BigDecimal getVotedSize() {
        return new BigDecimal(redisTemplate.opsForSet().size(CUT_SONG_KEY));
    }

    public void sendVoteNum(WebSocketSession session) {
        BigDecimal size = getVotedSize();
        if (size == null) {
            size = new BigDecimal(0);
        }
        WsSessionManager.sendMessage(session, new MessageVO("cut", String.valueOf(size)));
    }

    public void sendVoteNum() {
        BigDecimal size = getVotedSize();
        if (size == null) {
            size = new BigDecimal(0);
        }
        WsSessionManager.broadcast(new MessageVO("cut", String.valueOf(size)));
    }
}

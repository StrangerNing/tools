package me.znzn.tools.module.music.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/17
 */
@Slf4j
public class WsSessionManager {

    public static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> USER_NICKNAME = new ConcurrentHashMap<>();

    public static void add(String key, WebSocketSession session) {
        if (SESSION_POOL.containsKey(key)) {
            SESSION_POOL.replace(key, session);
        } else {
            SESSION_POOL.put(key, session);
        }
    }

    public static void remove(String key) {
        SESSION_POOL.remove(key);
    }

    public static void closeAndRemove(String key) {
        WebSocketSession session = SESSION_POOL.remove(key);
        if (null != session) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("关闭Session失败x", e);
            }
        }
    }

    public static WebSocketSession get(String key) {
        return SESSION_POOL.get(key);
    }

    public static String getToken(WebSocketSession session) {
        return session.getId();
//        List<String> cookies = session.getHandshakeHeaders().get("cookie");
//        assert cookies != null;
//        for (String cookie : cookies) {
//            String[] sp = cookie.split(";");
//            for (String s : sp) {
//                if (s.trim().startsWith("JSESSIONID")) {
//                    return s.substring(11);
//                }
//            }
//        }
//        return null;
    }

    public static void setUserNickname(WebSocketSession session, String nickName) {
        String token = getToken(session);
        if (token != null) {
            if (USER_NICKNAME.containsKey(token)) {
                USER_NICKNAME.replace(token, nickName);
            } else {
                USER_NICKNAME.put(token, nickName);
            }
        }
    }

    public static String getUserNickName(WebSocketSession session) {
        String token = getToken(session);
        if (token != null) {
            return USER_NICKNAME.get(token);
        }
        return null;
    }
}

package me.znzn.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.MessageVO;
import me.znzn.tools.module.music.entity.MusicInfoVO;
import me.znzn.tools.module.music.entity.MusicPushVO;
import me.znzn.tools.module.music.entity.MusicUrlVO;
import me.znzn.tools.module.music.websocket.WsSessionManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/7/23
 */
@Slf4j
public class MusicUtil {

    private static String API_URL = "https://cdn.zerodream.net/netease/api.php";

    public static String search(String name, String source) {
        String entity = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", source));
            list.add(new BasicNameValuePair("types", "search"));
            list.add(new BasicNameValuePair("count", "10"));
            list.add(new BasicNameValuePair("pages", "1"));
            list.add(new BasicNameValuePair("name", name));
            uriBuilder.setParameters(list);
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            entity = EntityUtils.toString(httpEntity);
        } catch (URISyntaxException u) {
            log.error("URI解析异常", u);
        } catch (IOException i) {
            log.error("IO异常", i);
        }
        return entity;
    }

    public static List<MusicInfoVO> searchToVO(String name) {
        String entity = search(name, "netease");
        return JSONArray.parseArray(entity, MusicInfoVO.class);
    }

    public static List<MusicInfoVO> searchBySourceToVO(String name, String type) {
        String entity = search(name, type);
        return JSONArray.parseArray(entity, MusicInfoVO.class);
    }

    public static MusicUrlVO getMusicUrl(String id, String source) {
        byte[] entity = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", source));
            list.add(new BasicNameValuePair("types", "url"));
            list.add(new BasicNameValuePair("id", String.valueOf(id)));
            uriBuilder.setParameters(list);
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            entity = EntityUtils.toByteArray(httpEntity);
        } catch (URISyntaxException u) {
            log.error("URI解析异常", u);
        } catch (IOException i) {
            log.error("IO异常");
        }
        return entity == null ? new MusicUrlVO() : JsonUtils.toObject(entity, MusicUrlVO.class);
    }

    public static String getMusicLrcs(String id, String source) {
        String lrc = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", source));
            list.add(new BasicNameValuePair("types", "lyric"));
            list.add(new BasicNameValuePair("id", String.valueOf(id)));
            uriBuilder.setParameters(list);
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            lrc = EntityUtils.toString(httpEntity);
        } catch (URISyntaxException u) {
            log.error("URI解析失败", u);
        } catch (IOException i) {
            log.error("IO异常", i);
        }
        if (lrc != null) {
            lrc = JSON.parseObject(lrc).getString("lyric");
            lrc = null == lrc ? "[00:01.00]暂无歌词" : lrc;
        }
        return lrc;
    }

    public static String getMusicImage(String id, String source) {
        String image = null;
        URIBuilder uriBuilder = null;
        try {
            if ("netease".equals(source)) {
                uriBuilder = new URIBuilder(API_URL);
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("source", source));
                list.add(new BasicNameValuePair("types", "pic"));
                list.add(new BasicNameValuePair("id", id));
                uriBuilder.setParameters(list);

            } else if ("kugou".equals(source)) {
                uriBuilder = new URIBuilder("http://api.63code.com/kugou/api.php");
                uriBuilder.setParameter("hash", id);
            }
            if (uriBuilder != null) {
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(uriBuilder.build());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                image = EntityUtils.toString(httpEntity);
            }
        } catch (URISyntaxException u) {
            log.error("URI解析失败", u);
        } catch (IOException i) {
            log.error("IO异常", i);
        }
        if (image != null) {
            image = "netease".equals(source) ? JSON.parseObject(image).getString("url") : JSON.parseObject(image).getString("pic");
        }
        return image;
    }

    public static Double getMusicLength(String songUrl) {
        Double time = null;
//        try {
//            URL url = new URL(songUrl);
//            URLConnection con = url.openConnection();
//            int b = con.getContentLength();
//            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
//            Bitstream bt = new Bitstream(bis);
//            Header h = bt.readFrame();
//            time = (double)h.total_ms(b);
//        } catch (MalformedURLException m) {
//            log.error("URL形式错误", m);
//        } catch (IOException i) {
//            log.error("链接URL出错，将依赖客户端上传获取音乐长度");
//        } catch (BitstreamException b) {
//            log.error("远程文件帧读取失败", b);
//        }
        return time;
    }

    public static MusicPushVO getMusic(MusicInfoVO music) {
        MusicUrlVO musicUrl = MusicUtil.getMusicUrl(music.getId(), music.getSource());
        MusicPushVO musicPushVO = new MusicPushVO();
        musicPushVO.setType("music");
        musicPushVO.setId(music.getId());
        musicPushVO.setName(music.getName());
        musicPushVO.setAlbum(music.getAlbum());
        musicPushVO.setArtists(music.getArtist());
        musicPushVO.setFile(musicUrl.getUrl());
        musicPushVO.setImage(MusicUtil.getMusicImage(music.getPic_id(), music.getSource()));
        musicPushVO.setCurrent("0");
        musicPushVO.setLrcs(MusicUtil.getMusicLrcs(music.getId(), music.getSource()));
        musicPushVO.setLength(MusicUtil.getMusicLength(musicUrl.getUrl()));
        return musicPushVO;
    }
}

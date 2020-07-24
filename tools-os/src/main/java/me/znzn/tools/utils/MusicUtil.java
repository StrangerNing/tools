package me.znzn.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.module.music.entity.MusicInfoVO;
import me.znzn.tools.module.music.entity.MusicUrlVO;
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

import java.io.IOException;
import java.net.URISyntaxException;
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

    public static String search(String name) {
        String entity = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", "netease"));
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
        String entity = search(name);
        return JSONArray.parseArray(entity, MusicInfoVO.class);
    }

    public static MusicUrlVO getMusicUrl(Long id) {
        byte[] entity = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", "netease"));
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

    public static String getMusicLrcs(Long id) {
        String lrc = null;
        try {
            String NeteaseUrl = "https://music.163.com/api/song/lyric";
            URIBuilder uriBuilder = new URIBuilder(NeteaseUrl);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("os","pc"));
            list.add(new BasicNameValuePair("lv", "-1"));
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
            JSONObject jsonObject = JSON.parseObject(lrc).getJSONObject("lrc");
            lrc = null == jsonObject ? "[00:01.00]暂无歌词" : jsonObject.getString("lyric");
        }
        return lrc;
    }

    public static String getMusicImage(String id) {
        String image = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(API_URL);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("source", "netease"));
            list.add(new BasicNameValuePair("types", "pic"));
            list.add(new BasicNameValuePair("id", id));
            uriBuilder.setParameters(list);
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            image = EntityUtils.toString(httpEntity);
        } catch (URISyntaxException u) {
            log.error("URI解析失败", u);
        } catch (IOException i) {
            log.error("IO异常", i);
        }
        if (image != null) {
            image = JSON.parseObject(image).getString("url");
        }
        return image;
    }
}

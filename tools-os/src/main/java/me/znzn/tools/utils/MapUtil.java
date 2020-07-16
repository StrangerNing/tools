package me.znzn.tools.utils;

import com.alibaba.druid.support.http.util.IPAddress;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.BMapModel;
import me.znzn.tools.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/28
 * @since 1.0
 */
@Slf4j
public class MapUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

    public static String getIpAddress(HttpServletRequest request) {
        log.info("请求头：{}",request.toString());
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info("访问者ip为：{}", ip);
        return ip;
    }

    public static BMapModel decodeIP(String ip) {
        //拼接请求
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(CommonConstant.MAP_REQUEST_PREFIX);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ip", ip));
        params.add(new BasicNameValuePair("ak", CommonConstant.MAP_AK));
        params.add(new BasicNameValuePair("coor", "bd09ll"));
        String decode = "";
        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity res = response.getEntity();
            decode = EntityUtils.toString(res);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("不支持的字符集", e);
        } catch (IOException io) {
            LOGGER.error("IOException", io);
        }
        return JsonUtils.toObject(decode.getBytes(), BMapModel.class);
    }

}

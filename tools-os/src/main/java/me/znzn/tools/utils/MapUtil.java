package me.znzn.tools.utils;

import com.alibaba.druid.support.json.JSONUtils;
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
public class MapUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

    public static String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    public static BMapModel decodeIP(String ip) {
        //拼接请求url
//        String url = StringUtil.parseBrace(CommonConstant.MAP_REQUEST_PREFIX, ip, CommonConstant.MAP_AK);
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

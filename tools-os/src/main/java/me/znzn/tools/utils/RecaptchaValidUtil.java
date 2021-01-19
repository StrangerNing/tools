package me.znzn.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.constant.CommonConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/19
 */
@Slf4j
public class RecaptchaValidUtil {

    public static Boolean valid(String token) {
        try {
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("secret", CommonConstant.CAPTCHA_SECRET));
            urlParameters.add(new BasicNameValuePair("response", token));

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(CommonConstant.CAPTCHA_END_URL);
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = httpClient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            String entity = EntityUtils.toString(httpEntity);
            JSONObject responseObject = JSONObject.parseObject(entity);
            log.info("recaptcha校验结果返回:{}", entity);
            return (Boolean)responseObject.get("success");
        } catch (UnsupportedEncodingException ue) {
            log.error("不支持的参数编码", ue);
        } catch (ClientProtocolException cpe) {
            log.error("与recaptcha协议异常", cpe);
        } catch (IOException io) {
            log.error("IO异常,", io);
        }
        return Boolean.FALSE;
    }
}

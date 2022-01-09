package me.znzn.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.exception.BusinessException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static me.znzn.tools.common.constant.CommonConstant.GOOGLE_VERIFY;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2022/1/7
 */
@Slf4j
public class GoogleIdTokenVerifyUtil {

    public static class GoogleUser {
        private String sub;

        private String email;

        private String name;

        private String picture;

        public String getSub() {
            return sub;
        }

        public void setSub(String id) {
            this.sub = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }

    public static GoogleUser verify(String idToken) {
        JSONObject jsonObject = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(GOOGLE_VERIFY);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("id_token", idToken));
            uriBuilder.setParameters(list);
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String entity = EntityUtils.toString(httpEntity);
            jsonObject = JSON.parseObject(entity);
            String sub = (String) jsonObject.get("sub");
            if (sub == null) {
                throw new BusinessException("登录失败");
            }
        } catch (URISyntaxException u) {
            log.error("URI解析异常", u);
        } catch (IOException i) {
            log.error("IO异常", i);
        }

        GoogleUser googleUser = new GoogleUser();
        if (jsonObject != null) {
            googleUser.setEmail(jsonObject.getString("email"));
            googleUser.setSub(jsonObject.getString("sub"));
            googleUser.setName(jsonObject.getString("name"));
            googleUser.setPicture(jsonObject.getString("picture"));
        }
        return googleUser;
    }
}

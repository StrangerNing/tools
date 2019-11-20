package me.znzn.tools.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json工具类
 * @author liuying
 *
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    static {
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * 将json反序列化成对象(不支持泛型对象)
     *
     * @param <T> 响应对象类型泛型
     * @param byteArray 数据
     * @param clazz 接收对象类型
     * @return
     */
	public static <T> T toObject(byte[] byteArray,Class<? extends T> clazz) {
    	try {
			return (T) objectMapper.readValue(byteArray, clazz);
		} catch (Exception e) {
			logger.error("反序列化失败", e);
			throw new RuntimeException(e);
		}
    }
}

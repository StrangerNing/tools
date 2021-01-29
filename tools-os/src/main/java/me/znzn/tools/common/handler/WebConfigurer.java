package me.znzn.tools.common.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhuzening
 * @date 2019/11/19
 * @since 1.0
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Value("${spring.mvc.static-path-pattern}")
    private String staticPathPattern;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**").order(1);
    }
}

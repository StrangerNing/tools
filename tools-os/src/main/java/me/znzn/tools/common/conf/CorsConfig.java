package me.znzn.tools.common.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author zening.zhu
 * @version v1.0
 * @date 2019/4/10
 */

@Configuration
public class CorsConfig {
    @Bean
    public FilterRegistrationBean corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置你要允许的网站域名，如果全允许则设为 *
        config.addAllowedOrigin("http://admin.znzn.me");
        config.addAllowedOrigin("https://admin.znzn.me");
        config.addAllowedOrigin("http://music.znzn.me");
        config.addAllowedOrigin("https://music.znzn.me");
        config.addAllowedOrigin("https://coucou.pet");
        config.addAllowedOrigin("https://www.coucou.pet");
        config.addAllowedOrigin("https://admin.coucou.pet");
        config.addAllowedOrigin("https://edchu.cn");
        config.addAllowedOrigin("https://www.edchu.cn");
        config.addAllowedOrigin("https://ndwz.cc");
        config.addAllowedOrigin("http://localhost");
        // 如果要限制 HEADER 或 METHOD 请自行更改
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 这个顺序很重要哦，为避免麻烦请设置在最前
        bean.setOrder(0);
        return bean;
    }
}

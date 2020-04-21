package me.znzn.tools.module.api.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.module.api.entity.GenerateShortUrlVO;
import me.znzn.tools.module.api.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/21
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private ApiService apiService;

    @GetMapping("/url/get")
    public Result getShortUrl(GenerateShortUrlVO shortUrlVO) {
        return Result.success(apiService.getShortUrl(shortUrlVO));
    }
}

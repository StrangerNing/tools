package me.znzn.tools.module.api.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.api.entity.GenerateShortUrlVO;
import me.znzn.tools.module.api.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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
    public ResponseEntity getShortUrl(GenerateShortUrlVO shortUrlVO) {
        return ResultPageUtil.success(apiService.getShortUrl(shortUrlVO));
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody Map<String, String> file) {
        return ResultPageUtil.success(apiService.uploadFile(file));
    }

    @PostMapping("/async/ig")
    public ResponseEntity asyncIg(@RequestBody Map<String, String> file) {
        apiService.asyncIg(file);
        return ResultPageUtil.success();
    }
}

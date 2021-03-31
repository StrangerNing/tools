package me.znzn.tools.module.oss.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import me.znzn.tools.utils.MultipartFileUtil;
import me.znzn.tools.utils.UploadFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
@Slf4j
@RestController
public class UploadController {
    @Resource
    private FileService fileService;

    @GetMapping("/file/getUrl/**")
    public ModelAndView getFileUrl(HttpServletRequest request) {
        try {
            String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
            String bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

            String name = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

            FileReturnVo file = fileService.getFile(name);
            String url = file.getUrl();

            String referer = request.getHeader("Referer");
            if (org.springframework.util.StringUtils.isEmpty(referer)) {
                ModelAndView modelAndView = new ModelAndView("preview_file");
                modelAndView.addObject("url", url);
                return modelAndView;
            }
            return new ModelAndView("redirect:" + url);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("dwz/404");
        }
    }

    @PostMapping("/upload/avatar")
    public ResponseEntity avatar(@RequestParam("file") MultipartFile file) {
        String type = MultipartFileUtil.getExtFilename(file);
        if (!".jpg".equals(type) && !".png".equals(type)) {
            return ResultPageUtil.error("只能上传\"jpg\"、\"png\"格式的图片");
        }
        FileReturnVo fileReturnVo = insertFile(file, OssFileTypeEnum.AVATAR, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
        return ResultPageUtil.success(fileReturnVo);
    }

    @PostMapping("/upload/image")
    public ResponseEntity image(@RequestParam("file") MultipartFile file) {
        String type = MultipartFileUtil.getExtFilename(file);
        if (!".jpg".equals(type) && !".png".equals(type) && !".gif".equals(type) && !".bmp".equals(type)) {
            return ResultPageUtil.error("只能上传图片");
        }
        FileReturnVo fileReturnVo = insertFile(file, OssFileTypeEnum.IMAGE, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
        return ResultPageUtil.success(fileReturnVo);
    }

    @PostMapping("/upload/blog")
    public ResponseEntity blog(@RequestParam("file") MultipartFile file) {
        String type = MultipartFileUtil.getExtFilename(file);
        if (!".jpg".equals(type) && !".png".equals(type) && !".gif".equals(type) && !".bmp".equals(type)
                && !".mp4".equals(type) && !".mpg".equals(type) && !".wmv".equals(type)) {
            return ResultPageUtil.error("只能上传图片和视频");
        }
        FileReturnVo fileReturnVo = insertFile(file, OssFileTypeEnum.BLOG, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
        return ResultPageUtil.success(fileReturnVo);
    }

    @PostMapping("/upload/file")
    public ResponseEntity file(@RequestParam("file") MultipartFile file) {
        FileReturnVo fileReturnVo = insertFile(file, OssFileTypeEnum.FILE, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
        return ResultPageUtil.success(fileReturnVo);
    }

    @PostMapping("/upload/blog/base64")
    public ResponseEntity base64(@RequestBody Map<String, String> file) {
        String base64 = file.get("base64");
        String filename = file.get("filename");
        byte[] data = Base64.decode(base64);
        if (StringUtils.isEmpty(filename)) {
            throw new BusinessException("需要文件名");
        }
        String suffix = filename.substring(filename.lastIndexOf("."));
        InputStream inputStream = new ByteArrayInputStream(data);
        FileReturnVo fileReturnVo = insertFile(inputStream, suffix, OssFileTypeEnum.BLOG, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
        return ResultPageUtil.success(fileReturnVo);
    }

    private FileReturnVo insertFile(MultipartFile file, OssFileTypeEnum fileType, Long expiration) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        String fileName = UploadFileUtil.uploadOSS(file, user, fileType);
        String url = UploadFileUtil.getFileUrl(fileName, expiration);
        FileReturnVo fileReturnVo = new FileReturnVo();
        fileReturnVo.setName(fileName);
        fileReturnVo.setUrl(url);
        fileService.insertFile(fileName, fileType, user);
        return fileReturnVo;
    }

    private FileReturnVo insertFile(InputStream inputStream, String suffix, OssFileTypeEnum fileType, Long expiration) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        String fileName = UploadFileUtil.uploadOSS(inputStream, suffix, user, fileType);
        String url = UploadFileUtil.getFileUrl(fileName, expiration);
        FileReturnVo fileReturnVo = new FileReturnVo();
        fileReturnVo.setName(fileName);
        fileReturnVo.setUrl(url);
        fileService.insertFile(fileName, fileType, user);
        return fileReturnVo;
    }
}

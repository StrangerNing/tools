package me.znzn.tools.module.oss.controller;

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
import me.znzn.tools.utils.UploadFileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
@Slf4j
@RestController()
public class UploadController {
    @Resource
    private FileService fileService;

    @PostMapping("/upload/avatar")
    public ResponseEntity avatar(@RequestParam("file") MultipartFile file) {
        try {
            String type = FileTypeUtil.getType(file.getInputStream());
            if (!"jpg".equals(type) && !"png".equals(type)) {
                throw new BusinessException("只能上传\"jpg\"、\"png\"格式的图片");
            }
            UserInfoVO user = LoginUserUtil.getSessionUser();
            String fileName = UploadFileUtil.uploadOSS(file, user, OssFileTypeEnum.AVATAR);
            String url = UploadFileUtil.getFileUrl(fileName, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
            FileReturnVo fileReturnVo = new FileReturnVo();
            fileReturnVo.setName(fileName);
            fileReturnVo.setUrl(url);
            fileService.insertFile(fileName, OssFileTypeEnum.AVATAR, user);
            return ResultPageUtil.success(fileReturnVo);
        } catch (IOException io) {
            log.error("获取文件错误，IO异常");
            return ResultPageUtil.error("获取文件错误，IO异常");
        }
    }

    @PostMapping("/upload/image")
    public ResponseEntity image(@RequestParam("file") MultipartFile file) {
        try {
            String type = FileTypeUtil.getType(file.getInputStream());
            if (!"jpg".equals(type) && !"png".equals(type) && !"gif".equals(type) && !"bmp".equals(type)) {
                throw new BusinessException("只能上传图片");
            }
            UserInfoVO user = LoginUserUtil.getSessionUser();
            String fileName = UploadFileUtil.uploadOSS(file, user, OssFileTypeEnum.IMAGE);
            String url = UploadFileUtil.getFileUrl(fileName, Long.valueOf(CommonConstant.OSS_URL_EXPIRATION));
            FileReturnVo fileReturnVo = new FileReturnVo();
            fileReturnVo.setName(fileName);
            fileReturnVo.setUrl(url);
            fileService.insertFile(fileName, OssFileTypeEnum.IMAGE, user);
            return ResultPageUtil.success(fileReturnVo);
        } catch (IOException io) {
            log.error("获取文件错误，IO异常");
            return ResultPageUtil.error("获取文件错误，IO异常");
        }
    }
}

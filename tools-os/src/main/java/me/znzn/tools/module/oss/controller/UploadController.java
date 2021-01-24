package me.znzn.tools.module.oss.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.enums.OssBucketNameEnum;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import me.znzn.tools.utils.UploadFileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/1/24
 */
@RestController()
public class UploadController {

    @PostMapping("/upload/avatar")
    public ResponseEntity avatar(@RequestParam("file") MultipartFile file) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        String fileName = UploadFileUtil.uploadOSS(file, user, OssBucketNameEnum.AVATAR);
        String url = UploadFileUtil.getFileUrl(fileName, 3600 * 1000L);
        FileReturnVo fileReturnVo = new FileReturnVo();
        fileReturnVo.setFilename(fileName);
        fileReturnVo.setUrl(url);
        return ResultPageUtil.success(fileReturnVo);
    }
}

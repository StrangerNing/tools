package me.znzn.tools.module.oss.controller;


import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.module.oss.entity.form.FileForm;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/01/27 11:23:13
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/get/{id}")
    public ResponseEntity getFile(@PathVariable Long id) {
        return ResultPageUtil.success(fileService.getFile(id));
    }

    @GetMapping("/image/list")
    public ResponseEntity imageList(FileForm file) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        file.setCreateAccount(user.getId());
        file.setType(OssFileTypeEnum.IMAGE.getIndex());
        ResultPage page = fileService.getFileList(file);
        return ResultPageUtil.successWithPage(page.getList(), page.getTotalCount(), page.getCurrentPage());
    }

    @GetMapping("/del/{id}")
    public ResponseEntity delImage(@PathVariable Long id) {
        fileService.delFile(id);
        return ResultPageUtil.success();
    }

}

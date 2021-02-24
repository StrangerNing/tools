package me.znzn.tools.module.oss.controller;


import cn.hutool.system.UserInfo;
import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.module.oss.entity.form.FileForm;
import me.znzn.tools.module.oss.entity.po.File;
import me.znzn.tools.module.oss.entity.vo.FileReturnVo;
import me.znzn.tools.module.oss.service.FileService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2021/01/27 11:23:13
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/getUrl/**")
    public ModelAndView getFileUrl(HttpServletRequest request) {
        try {
            String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
            String bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

            String name = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

            FileReturnVo file = fileService.getFile(name);
            String url = file.getUrl();

            String referer = request.getHeader("Referer");
            if (StringUtils.isEmpty(referer)) {
                ModelAndView modelAndView = new ModelAndView("preview_file");
                modelAndView.addObject("url", url);
                return modelAndView;
            }
            return new ModelAndView("redirect:" + url);
        } catch (Exception e) {
            return new ModelAndView("404");
        }
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity getFile(@PathVariable Long id) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        return ResultPageUtil.success(fileService.getFile(id, user));
    }

    @GetMapping("/image/list")
    @ResponseBody
    public ResponseEntity imageList(FileForm file) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        file.setCreateAccount(user.getId());
        file.setType(OssFileTypeEnum.IMAGE.getIndex());
        ResultPage page = fileService.getFileList(file);
        return ResultPageUtil.successWithPage(page.getList(), page.getTotalCount(), page.getCurrentPage());
    }

    @GetMapping("/del/{id}")
    @ResponseBody
    public ResponseEntity delImage(@PathVariable Long id) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        fileService.delFile(id, user);
        return ResultPageUtil.success();
    }

    @GetMapping("/del")
    @ResponseBody
    public ResponseEntity delImage(String name) {
        UserInfoVO user = LoginUserUtil.getSessionUser();
        fileService.delFile(name, user);
        return ResultPageUtil.success();
    }

}

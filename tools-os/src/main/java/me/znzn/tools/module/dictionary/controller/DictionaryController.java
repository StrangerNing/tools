package me.znzn.tools.module.dictionary.controller;

import me.znzn.tools.common.component.ResultPageUtil;
import me.znzn.tools.module.dictionary.entity.po.Dictionary;
import me.znzn.tools.module.dictionary.service.DictionaryService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */
@RestController
@RequestMapping("/constant")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @GetMapping("/update")
    public ResponseEntity refreshConstant() {
        dictionaryService.updateAllConstant();
        return ResultPageUtil.success();
    }

    @GetMapping("/query")
    public ResponseEntity getConstantList(Dictionary dictionary) {
        return ResultPageUtil.successWithPage(dictionaryService.getConstantList(dictionary), dictionaryService.countByProperty(dictionary), dictionary.getCurrentPage());
    }

    @PostMapping("/modify")
    public ResponseEntity updateConstant(@RequestBody Dictionary dictionary) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return ResultPageUtil.success(dictionaryService.updateConstant(dictionary, loginUser));
    }

    @PostMapping("/add")
    public ResponseEntity addConstant(@RequestBody Dictionary dictionary) {
        UserInfoVO loginUser = LoginUserUtil.getSessionUser();
        return ResultPageUtil.success(dictionaryService.addConstant(dictionary, loginUser));
    }
}

package me.znzn.tools.module.dictionary.controller;

import me.znzn.tools.common.component.Result;
import me.znzn.tools.module.dictionary.entity.po.Dictionary;
import me.znzn.tools.module.dictionary.service.DictionaryService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
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
    public Result refreshConstant() {
        dictionaryService.updateAllConstant();
        return Result.success();
    }

    @GetMapping("/query")
    public Result getConstantList(Dictionary dictionary) {
        return Result.success(dictionaryService.getConstantList(dictionary));
    }

    @PostMapping("/modify")
    public Result updateConstant(@RequestBody Dictionary dictionary) {
        UserInfoVO loginUser = LoginUserUtil.getLoginUser();
        return Result.success(dictionaryService.updateConstant(dictionary, loginUser));
    }

    @PostMapping("/add")
    public Result addConstant(@RequestBody Dictionary dictionary) {
        UserInfoVO loginUser = LoginUserUtil.getLoginUser();
        return Result.success(dictionaryService.addConstant(dictionary, loginUser));
    }
}

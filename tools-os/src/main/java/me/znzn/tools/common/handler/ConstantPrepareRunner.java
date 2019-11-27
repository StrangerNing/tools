package me.znzn.tools.common.handler;

import me.znzn.tools.module.dictionary.service.impl.DictionaryServiceImpl;
import me.znzn.tools.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */

@Component
@Order(value = 1)
public class ConstantPrepareRunner implements ApplicationRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConstantPrepareRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("正在初始化全局变量……");
        Long begin = System.currentTimeMillis();
        DictionaryServiceImpl dictionaryService = SpringUtil.getBean(DictionaryServiceImpl.class);
        dictionaryService.updateAllConstant();
        Long end = System.currentTimeMillis();
        LOGGER.info("初始化完成，用时{}ms",end-begin);
    }
}

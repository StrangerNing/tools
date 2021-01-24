package me.znzn.tools.module.dictionary.service.impl;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.dictionary.entity.po.Dictionary;
import me.znzn.tools.module.dictionary.entity.vo.DictionaryVO;
import me.znzn.tools.module.dictionary.mapper.DictionaryMapper;
import me.znzn.tools.module.dictionary.service.DictionaryService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LoginUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/27
 * @since 1.0
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Override
    public void updateAllConstant() {
        Dictionary dictionary = new Dictionary();
        dictionary.setStatus(StatusEnum.ENABLE.getIndex());
        List<Dictionary> constants = dictionaryMapper.selectByProperty(dictionary);
        Class clazz = CommonConstant.class;
        for (Dictionary constant : constants) {
            try {
                LOGGER.info("正在设定变量 {} ，变量值 {}",constant.getName(), constant.getValue());
                Field field = clazz.getField(constant.getName());
                field.set(null, constant.getValue());
            } catch (Exception e) {
                LOGGER.error("设定出错，请检查字段名和字段值！",e);
            }
        }
    }

    @Override
    public List<DictionaryVO> getConstantList(Dictionary dictionary) {
        return dictionaryMapper.selectConstantListByCondition(dictionary);
    }

    @Override
    public int countByProperty(Dictionary dictionary) {
        return dictionaryMapper.countByProperty(dictionary);
    }

    @Override
    public Boolean updateConstant(Dictionary dictionary, UserInfoVO loginUser) {
        dictionary.setModifyAccount(loginUser.getId());
        dictionary.setModifyTime(new Date());
        return dictionaryMapper.updateByCondition(dictionary) == 1;
    }

    @Override
    public Boolean addConstant(Dictionary dictionary, UserInfoVO userInfoVO) {
        Long operateId = userInfoVO.getId();
        Date operateTime = new Date();
        dictionary.setCreateAccount(operateId);
        dictionary.setCreateTime(operateTime);
        dictionary.setModifyAccount(operateId);
        dictionary.setModifyTime(operateTime);
        dictionary.setStatus(StatusEnum.ENABLE.getIndex());
        dictionary.setVersion(0);
        return dictionaryMapper.insertByProperty(dictionary).equals(1L);
    }
}

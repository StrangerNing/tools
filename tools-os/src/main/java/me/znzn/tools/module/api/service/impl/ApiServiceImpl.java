package me.znzn.tools.module.api.service.impl;

import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.api.entity.GenerateShortUrlVO;
import me.znzn.tools.module.api.service.ApiService;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.po.ApiKey;
import me.znzn.tools.module.user.mapper.ApiKeyMapper;
import me.znzn.tools.utils.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2020/4/21
 */
@Service
public class ApiServiceImpl implements ApiService {

    @Resource
    private ApiKeyMapper apiKeyMapper;

    @Resource
    private ShortUrlService shortUrlService;

    @Override
    public String getShortUrl(GenerateShortUrlVO shortUrlVO) {
        ValidatorUtil.validate(shortUrlVO);
        ApiKey query = new ApiKey();
        query.setAk(shortUrlVO.getAk());
        List<ApiKey> list = apiKeyMapper.selectByProperty(query);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException("ak错误，请确认后重试");
        }
        if (list.size() > 1) {
            throw new BusinessException("ak配置服务器内部出错，请联系网站管理员");
        }
        ApiKey apiKey = list.get(0);
        Long userId = apiKey.getCreateId();
        String url = shortUrlVO.getUrl();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginUrl(url);
        shortUrl.setStatus(StatusEnum.ENABLE.getIndex());
        shortUrl.setCreateAccount(userId);
        shortUrl.setCreateTime(new Date());
        return shortUrlService.saveUrl(shortUrl);
    }
}

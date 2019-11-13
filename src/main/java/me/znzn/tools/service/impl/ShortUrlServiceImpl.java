package me.znzn.tools.service.impl;

import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.entity.ShortUrl;
import me.znzn.tools.mapper.ShortUrlMapper;
import me.znzn.tools.service.ShortUrlService;
import me.znzn.tools.utils.LongNumUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Resource
    private ShortUrlMapper shortUrlMapper;

    @Override
    public String saveUrl(String url) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginUrl(url);
        shortUrl.setCreateTime(new Date());
        Long num = shortUrlMapper.insertByProperty(shortUrl);
        if (num.equals(1L)) {
            return LongNumUtil.encode(shortUrl.getId());
        }
        throw new BusinessException("保存失败");
    }

    @Override
    public String getOriginUrl(String shortUrl) {
        Long id = LongNumUtil.decode(shortUrl);
        ShortUrl result = shortUrlMapper.selectByPrimaryKey(id);
        return result.getOriginUrl();
    }
}

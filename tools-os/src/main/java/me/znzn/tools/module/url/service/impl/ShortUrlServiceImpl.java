package me.znzn.tools.module.url.service.impl;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.url.entity.constant.UrlRedisConstant;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.entity.vo.ShortUrlVO;
import me.znzn.tools.module.url.mapper.ShortUrlMapper;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.user.entity.enums.StatusEnum;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LongNumUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuzening
 * @date 2019/11/11
 * @since 1.0
 */

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    @Resource
    private ShortUrlMapper shortUrlMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String saveUrl(ShortUrl shortUrl) {
        if (StringUtils.isEmpty(shortUrl.getOriginUrl())) {
            throw new BusinessException("网址url不可为空！");
        }
        if (!shortUrl.getOriginUrl().matches("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?")) {
            throw new BusinessException("网址url需以\"http\"或\"https\"开头！");
        }
        shortUrl.setCreateTime(new Date());
        shortUrl.setStatus(StatusEnum.ENABLE.getIndex());
        Long num = shortUrlMapper.insertByProperty(shortUrl);
        if (num.equals(1L)) {
            return CommonConstant.SHORT_URL_PREFIX + LongNumUtil.encode(shortUrl.getId());
        }
        throw new BusinessException("保存失败");
    }

    @Override
    public ShortUrl getOriginUrl(String shortUrl) {
        Long id = LongNumUtil.decode(shortUrl);
        ShortUrl url = (ShortUrl)redisTemplate.opsForValue().get(UrlRedisConstant.SHORT_URL_KEY + id);
        if (url == null) {
            ShortUrl dbUrl = shortUrlMapper.selectByPrimaryKey(id);
            if (dbUrl != null) {
                redisTemplate.opsForValue().set(UrlRedisConstant.SHORT_URL_KEY + id, dbUrl, Long.parseLong(CommonConstant.SHORT_URL_CACHE_TIME), TimeUnit.DAYS);
            }
            return dbUrl;
        } else {
            redisTemplate.expire(UrlRedisConstant.SHORT_URL_KEY + id, Long.parseLong(CommonConstant.SHORT_URL_CACHE_TIME), TimeUnit.DAYS);
        }
        return url;
    }

    @Override
    public ResultPage getUserUrlList(UserInfoVO userInfoVO, ShortUrl page) {
        Long userId = userInfoVO.getId();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCreateAccount(userId);
        shortUrl.setCurrentPage(page.getCurrentPage());
        shortUrl.setLimit(page.getLimit());
        List<ShortUrlVO> result = shortUrlMapper.selectShortUrlByCondition(shortUrl);
        result.forEach(item -> item.setShortUrl(CommonConstant.SHORT_URL_PREFIX + LongNumUtil.encode(item.getId())));
        return new ResultPage(shortUrlMapper.countByProperty(shortUrl), page.getLimit(), page.getCurrentPage(), result);
    }

    @Override
    public Boolean updateShortUrlStatus(ShortUrl shortUrl) {
        ShortUrl update = new ShortUrl();
        update.setId(shortUrl.getId());
        update.setStatus(shortUrl.getStatus());
        return shortUrlMapper.updateByPrimaryKey(update) == 1;
    }
}

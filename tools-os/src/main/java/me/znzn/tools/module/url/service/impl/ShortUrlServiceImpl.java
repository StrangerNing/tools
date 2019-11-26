package me.znzn.tools.module.url.service.impl;

import me.znzn.tools.common.component.ResultPage;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.url.entity.po.ShortUrl;
import me.znzn.tools.module.url.entity.vo.ShortUrlVO;
import me.znzn.tools.module.url.mapper.ShortUrlMapper;
import me.znzn.tools.module.url.service.ShortUrlService;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import me.znzn.tools.utils.LongNumUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    public String saveUrl(ShortUrl shortUrl) {
        shortUrl.setCreateTime(new Date());
        Long num = shortUrlMapper.insertByProperty(shortUrl);
        if (num.equals(1L)) {
            return LongNumUtil.encode(shortUrl.getId());
        }
        throw new BusinessException("保存失败");
    }

    @Override
    public ShortUrl getOriginUrl(String shortUrl) {
        Long id = LongNumUtil.decode(shortUrl);
        return shortUrlMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResultPage getUserUrlList(UserInfoVO userInfoVO, ShortUrl page) {
        Long userId = userInfoVO.getId();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCreateAccount(userId);
        shortUrl.setCurrentPage(page.getCurrentPage());
        shortUrl.setLimit(page.getLimit());
        List<ShortUrlVO> result = shortUrlMapper.selectShortUrlByCondition(shortUrl);
        result.forEach(item -> item.setShortUrl(LongNumUtil.encode(item.getId())));
        return new ResultPage(shortUrlMapper.countByProperty(shortUrl), page.getLimit(), page.getCurrentPage(), result);
    }
}

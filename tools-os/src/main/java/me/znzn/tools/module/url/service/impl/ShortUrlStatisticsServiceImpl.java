package me.znzn.tools.module.url.service.impl;

import me.znzn.tools.common.component.BMapModel;
import me.znzn.tools.common.constant.CommonConstant;
import me.znzn.tools.module.url.entity.form.VisitHisForm;
import me.znzn.tools.module.url.entity.po.VisitHis;
import me.znzn.tools.module.url.entity.vo.VisitHisVO;
import me.znzn.tools.module.url.mapper.VisitHisMapper;
import me.znzn.tools.module.url.service.ShortUrlStatisticsService;
import me.znzn.tools.utils.LongNumUtil;
import me.znzn.tools.utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/28
 * @since 1.0
 */

@Service
public class ShortUrlStatisticsServiceImpl implements ShortUrlStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortUrlStatisticsServiceImpl.class);

    @Resource
    private VisitHisMapper visitHisMapper;

    @Async
    @Override
    public void saveVisitHistory(String shortUrl, HttpServletRequest request) {
//        String ip = MapUtil.getIpAddress(request);
        String ip = "222.76.251.164";
        Date now = new Date();
        Long shortUrlId = LongNumUtil.decode(shortUrl);
        //检查是否在设定的有效访问间隔时间内该ip是否已访问过该短链接
        VisitHis query = new VisitHis();
        query.setUrlId(shortUrlId);
        query.setIp(ip);
        List<VisitHis> visitHisHas = visitHisMapper.selectByProperty(query);
        boolean isValid = true;
        if (!ListUtils.isEmpty(visitHisHas)) {
            VisitHis latest = visitHisHas.get(0);
            long interval = now.getTime() - latest.getCreateTime().getTime();
            isValid = interval > Long.parseLong(CommonConstant.VALID_VISIT_INTERVAL);
        }
        if (isValid) {
            BMapModel bMapModel = MapUtil.decodeIP(ip);
            VisitHis visitHis = new VisitHis();
            visitHis.setUrlId(shortUrlId);
            visitHis.setIp(ip);
            visitHis.setAddress(bMapModel.getAddress());
            visitHis.setLng(new BigDecimal(bMapModel.getContent().getPoint().getX()));
            visitHis.setLat(new BigDecimal(bMapModel.getContent().getPoint().getY()));
            visitHis.setCreateTime(new Date());
            visitHisMapper.insertByProperty(visitHis);
        } else {
            LOGGER.error("ip:{}在有效间隔时间:{} ms内已访问过URL:{}，不再重复记录", ip, CommonConstant.VALID_VISIT_INTERVAL, shortUrlId);
        }
    }

    @Override
    public List<VisitHisVO> getVisitHistoryListByUrlId(VisitHisForm query) {
        return visitHisMapper.getVisitMapCount(query);
    }
}

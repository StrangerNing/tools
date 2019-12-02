package me.znzn.tools.module.url.service;

import me.znzn.tools.module.url.entity.form.VisitHisForm;
import me.znzn.tools.module.url.entity.po.VisitHis;
import me.znzn.tools.module.url.entity.vo.VisitHisVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhuzening
 * @date 2019/11/28
 * @since 1.0
 */
public interface ShortUrlStatisticsService {

    /**
     * 保存访问记录
     * @param shortUrl 短链接
     * @param request 访问请求
     */
    void saveVisitHistory(String shortUrl, HttpServletRequest request);

    /**
     * 根据短链接id获取统计列表
     * @param visitHisForm 短链接id
     * @return java.util.List 统计列表
     */
    List<VisitHisVO> getVisitHistoryListByUrlId(VisitHisForm visitHisForm);

}

package me.znzn.tools.module.url.entity.po;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * (VisitHis)表实体类
 *
 * @author zening.zhu
 * @version 1.0.0
 * @date 2019/11/29 11:19:57
 */
public class VisitHis implements Serializable{
    private static final long serialVersionUID = 802792506394386125L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 短链接id
     */
    private Long urlId;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 地址
     */
    private String address;

    /**
     * 访问时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}

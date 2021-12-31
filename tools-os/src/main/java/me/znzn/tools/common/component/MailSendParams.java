package me.znzn.tools.common.component;

import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/8/2
 */
public class MailSendParams {
    private String to;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 友链取消原因
     */
    private String cancelReason;

    /**
     * 评论人
     */
    private String from;

    /**
     * 评论人头像
     */
    private String fromAvatar;

    /**
     * 文章题目
     */
    private String title;

    /**
     * 文章阅读时间
     */
    private Integer minutes;

    /**
     * 消息
     */
    private String message;

    /**
     * 文章内容
     */
    private List<String> contentList;

    /**
     * 退订eid
     */
    private String eid;

    private String href;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

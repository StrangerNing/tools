package me.znzn.tools.module.blog.entity.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhuzening
 * @version 1.0
 * @since 2021/12/30
 */
@Data
public class SubscribeManageForm {

    private List<Integer> subscribeTypes;

    @NotNull
    private String eid;
}

package me.znzn.tools.common.enums;


/**
 * @author
 * 通用成功枚举类
 */
public class SuccessEnum {

    /**
     * Description: 通用业务成功
     * All Rights Reserved.
     */
    public static enum CommonSuccess implements EnumValue {
       UPDATE("更新成功!",0),DELETE("删除成功!",1),OPERATE("操作成功!",2),CREATE("新增成功!",3);

        CommonSuccess(String name, int value) {
            this.name = name;
            this.value = value;
        }

        private String name;
        private int value;

        public int getValue() {
            return value;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return value;
        }
    }
}

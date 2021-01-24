package me.znzn.tools.common.enums;


/**
 * @author
 * 通用异常枚举类
 */
public class ExceptionEnum {


    /**
     * Description: 通用业务异常
     * All Rights Reserved.
     */
    public static enum CommonExpetion implements EnumValue {
        LOAD("加载数据失败!",0),
        CREATE("新增失败!",1),
        UPDATE("修改失败!",2),
        DELETE("删除失败!",3),
        OPERATE("操作失败!",4),
        SYSTEM("系统错误!",5),
        VALIDATE("参数校验不通过",6);
        CommonExpetion(String name, int value) {
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

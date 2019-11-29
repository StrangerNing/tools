package me.znzn.tools.common.component;

import lombok.Data;
import me.znzn.tools.common.handler.ConstantPrepareRunner;

/**
 * @author zhuzening
 * @date 2019/11/28
 * @since 1.0
 */
@Data
public class BMapModel {

    @Data
    public class Content {
        @Data
        public class Point {
            private String x;
            private String y;
        }
        @Data
        public class AddressDetail {
            private String city;
            private Integer city_code;
            private String district;
            private String province;
            private String street;
            private String street_number;
        }
        private String address;
        private AddressDetail address_detail;
        private Point point;
    }

    private String address;

    private Content content;

    private Integer status;
}

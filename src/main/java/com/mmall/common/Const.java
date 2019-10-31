package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currentUser";//当前用户

    //实时校验，检查用户名和密码是否存在的常量
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }


    //普通用户和管理员是一个组，可以用枚举，但会过于繁重复杂，可以通过一个内部接口类，将常量进行分组
    public interface Role{
        int ROLE_CUSTOMER = 1; //普通用户
        int ROLE_ADMIN = 0; //管理员
    }

    public enum ProductStatusEnum{

        ON_SALE(1,"在线");

        private String value;
        private int code;

        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}

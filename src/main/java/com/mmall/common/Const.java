package com.mmall.common;

public class Const {

    public static final String CURRENT_USER = "currentUser";//当前用户

    //实时校验，检查用户名和密码是否存在的常量
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    //普通用户和管理员是一个组，可以用枚举，但会过于繁重复杂，可以通过一个内部接口类，将常量进行分组
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
}

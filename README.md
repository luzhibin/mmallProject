# mmallProject
# mmall电商平台
# 接口
[门户——用户接口](#jump)
# <span id="jump">门户——用户接口</span>
## role字段：1代表普通用户，0代表管理员

## 1.登录
### /user/login.do post（代码需要post方式请求），开放get，方便调试
> request  

```
username,password
```

> response

* fail

 ```
 {
     "status": 1,
     "msg": "密码错误"
 }
 ```

* success

 ```
{
    "status": 0,
    "data": {
        "id": 123456,
        "username": "aaaa",
        "email": "aaaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1979048325000,
        "updateTime": 1979048325000
    }
}
 ```  
---
## 2.注册  
### /user/register.do  
> request  
```
username,password,email,phone
```
> response
* success
```
{
    "status": 0,
    "msg": "校验成功"
}
```  
* fail  
```
{
    "status": 1,
    "msg": "用户已存在"
}
```
---  
## 3.检查用户名是否有效
### /user/check_valid.do  
### /check_valid.do?str=admin&type=username就是检查用户名。
> request
```
str,type
str可以是用户名也可以是email。对应的type是username和email
```
> response
* success
```
{
    "status": 0,
    "msg": "校验成功"
}
```
* fail
```
{
    "status": 1,
    "msg": "用户已存在"
}
```
--
## 4.获取登录用户信息 
### /user/get_user_info.do
> request
``` 
无参数
```
> response
* success
```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 1,
        "createTime":1979048325000 ,
        "updateTime":1979048325000 
    }
}
```
* fail
```
{
    "status": 1,
    "msg": "用户未登录,无法获取当前用户信息"
}
```
---
## 5.登录中状态重置密码 
### /user/reset_password.do
> request
```
passwordOld,passwordNew
```
> response
* success
```
{
    "status": 0,
    "msg": "修改密码成功"
}
```
* fail
```
{
    "status": 1,
    "msg": "旧密码输入错误"
}
```

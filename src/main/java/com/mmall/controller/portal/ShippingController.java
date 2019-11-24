package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by luzhibin on 2019/11/23 16:45
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService ishippingService;


    //增加收货地址
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session , Shipping shipping){//SpringMVC中的一种绑定方式：直接绑定对象。对象数据绑定，用Shiping对象承载接收到的字段，SpringMVC会自动生成对象，并把对应的属性赋值
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ishippingService.add(user.getId(),shipping);
    }

    //删除收货地址
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ishippingService.del(user.getId(),shippingId);
    }

    //更新收货地址
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ishippingService.update(user.getId(),shipping);
    }

    //查询收货地址
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ishippingService.select(user.getId(),shippingId);
    }

    //分页的接口
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,//当前页
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,//每页的数量
                                         HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ishippingService.list(user.getId(),pageNum,pageSize);
    }
}

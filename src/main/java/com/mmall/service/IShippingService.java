package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by luzhibin on 2019/11/23 16:45
 */
public interface IShippingService {

    //新建地址
    ServerResponse add(Integer userId, Shipping shipping);

    //删除地址
    ServerResponse<String> del(Integer userId,Integer shippingId);

    //更新地址
    ServerResponse update(Integer userId,Shipping shipping);

    //查询地址
    ServerResponse<Shipping> select(Integer userId,Integer shippingId);

    //查询某个用户的所有收货地址
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}

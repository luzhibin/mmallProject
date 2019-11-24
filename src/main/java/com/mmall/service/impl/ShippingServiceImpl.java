package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luzhibin on 2019/11/23 16:46
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    //新增地址
    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0){
            //返回的id不需要额外放到新的对象里，直接用Map承载
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());//key，value
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    //删除地址
    public ServerResponse<String> del(Integer userId,Integer shippingId){
        //要防止横向越权，当用户登录后传一个不属于自己的shippingId，就会删除了其他人的数据
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if (resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    //更新地址
    public ServerResponse update(Integer userId,Shipping shipping){
        //要防止横向越权，当用户登录后传一个不属于自己的shippingId，就会删除了其他人的数据
        //更新时要指定userId 防止越权
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount>0){
            return ServerResponse.createBySuccess("更新地址失败");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    //查询地址
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
        //防止横向越权
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
    }

    //查询某个用户的所有收货地址
    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        //启动分页 PageHelper
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        //构造PageInfo
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}

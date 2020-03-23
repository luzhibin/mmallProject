package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * Created by luzhibin on 2020/2/23 23:21
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}

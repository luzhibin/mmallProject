package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by luzhibin on 2019/10/31 15:58
 */
public interface ICartService {
    ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> updateProduct(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteProduct(Integer id, String productIds);
}

package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by luzhibin on 2019/10/31 15:58
 */
public interface ICartService {
    ServerResponse<CartVo>list (Integer userId);

    ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> updateProduct(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteProduct(Integer id, String productIds);

    //商品全选或反选
    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    //查询当前用户的购物车中的商品数量
    ServerResponse<Integer> getCartProductCount(Integer userId);
}

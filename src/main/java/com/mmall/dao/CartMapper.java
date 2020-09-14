package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;
import sun.awt.image.IntegerComponentRaster;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    //按主键选择性更新
    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    //判断是否全选
    int selectCartProductCheckedStatusByUserId(Integer userId);

    //删除购物车商品
    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    //全选或反选商品
    int checkOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    //查询当前用户购物车中的商品总数量
    int selectCartProductCount(@Param("userId") Integer userId);


    //从购物车中获取已经勾选的产品
    List<Cart> selectCheckedCartByUserId(Integer userId);
}
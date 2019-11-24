package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    //删除收货地址
    int deleteByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);

    //更新收获地址
    int updateByShipping(Shipping record);

    //查询收货地址
    Shipping selectByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

    //通过userid查询该用户所有的收货地址
    List<Shipping> selectByUserId(@Param("userId")Integer userId);
}
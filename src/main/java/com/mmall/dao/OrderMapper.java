package com.mmall.dao;

import com.mmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    //根据orderNo和userId查询该订单是否存在
    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);

    Order selectByOrderNo(Long orderNo);
}
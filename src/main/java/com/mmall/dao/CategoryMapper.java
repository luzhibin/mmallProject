package com.mmall.dao;

import com.mmall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    //添加商品分类
    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //根据categoryId获取子节点平级category信息
    List<Category> selectCategoryChildrenByParentId(Integer parentId);
}
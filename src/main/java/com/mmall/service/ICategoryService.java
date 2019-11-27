package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    //增加商品分类
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    //根据品类名称获取子结点的平级品类信息
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}

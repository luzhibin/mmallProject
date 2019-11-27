package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    //日志
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private  CategoryMapper categoryMapper;

    //增加商品分类，参数：商品分类名称和父节点ID(默认值为0)
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount = categoryMapper.insert(category);
        if (rowCount>0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    //更新品类名称
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        //对参数进行判断和校验
        if (categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        //通过主键选择性更新，即通过主键更新分类名称
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    //根据分类名称获取子节点分类的平级分类信息，不递归
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        //做一个空判断 isEmpty不止判断了它本身是不是空，同时也判断了它是否是一个空的集合(即里面为0个元素的集合)
        if (CollectionUtils.isEmpty(categoryList)){
            //打印一行日志，因为如果得到一个空的没有子元素的集合，就不需要给前端返回
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 获取当前categoryId并且递归查询子结点的categoryId
     * @param categoryId
     * @return
     */
    //服务器内部调用的接口的实现类，根据categoryId查询子节点的categoryId，递归查询
    //拿到categoryId后，需要判断该节点下是否有子节点，如果有子节点，那么还需要判断子节点下是否还有子节点
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        this.findChildCategory(categorySet,categoryId);

        //返回值是categoryId的一个集合
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //使用递归算法，算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){  //set集合里是实体类对象的时候，需要重写equals和hashCode方法
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        //查找子节点，递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}










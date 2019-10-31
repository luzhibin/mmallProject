package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.utils.DateTimeUtils;
import com.mmall.utils.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    //用一个接口保存和更新商品
    public ServerResponse saveOrUpdateProduct(Product product){
        if (product != null){
            //判断子图是否为空
            if (StringUtils.isNotBlank(product.getSubImages())){//用StringUtils进行判断
                //如果子图不为空，就取第一个子图赋值给主图
               String[] subImageArray = product.getSubImages().split(",");//与前端约定用逗号分割
               if (subImageArray.length > 0){
                   product.setMainImage(subImageArray[0]);
               }
            }
            //更新 //更新时必须传入商品ID
            if(product.getId() != null ){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createBySuccess("更新商品失败");
            }else{  //  如果商品ID为空
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("新增商品成功");
                }
                return ServerResponse.createBySuccess("新增商品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新商品参数不正确");
    }

    //产品上下架业务逻辑，更新产品销售状态
    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0){
            return ServerResponse.createBySuccess("修改商品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改商品销售状态失败");
    }

    //泛型使用VO来填充
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId == null){ //如果productId为空，直接返回参数错误
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        //返回VO对象-- Value Object     //复杂情况下 pojo->bo(business object)->vo(view object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());

        productDetailVo.setStock(product.getStock());
        //imageHost  从配置文件中获取 让配置和代码分离 不需要把URL硬编码到代码当中
        //parentCategoryId
        //createTime
        //updateTime
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null ){
            //如果没有categoryId或者这个对象为null，就把ParentCategoryId赋值为0，默认是一个根节点
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());//如果不是空，就赋值当前对象的ParentId
        }

        productDetailVo.setCreateTime(DateTimeUtils.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtils.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponse getProductList(int pageNum,int pageSize){
        /**
         * pagehelper的使用方法
         * 1.startPage  记录一个开始
         * 2.填充自己的sql的查询逻辑
         * 3.pagehelper  收尾
         */
        //startPage
        PageHelper.startPage(pageNum,pageSize);
        //执行sql的逻辑
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem:productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //收尾
        PageInfo pageResult = new PageInfo(productList);
        //把List重置
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    //后台商品搜索功能
    public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){//要用分页，所以要放上PageInfo对象
        PageHelper.startPage(pageNum,pageSize);
        //对productName做一个判断
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem:productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //分页
        PageInfo pageResult = new PageInfo(productList);
        //把List重置
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已经下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductBykeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        //参数校验  关键字和分类ID是否为空
        if (StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        //该集合的作用：当分类的时候，传一个高级商品，比如电子产品，下一级还有手机，手机下一级又有其他分类
        //当传一个大的父类的时候，就要调用递归算法，把所有属于这个分类的子分类都遍历出来，并且加上该分类本身
        //把所有categoryId放到categoryIdList里，查询sql时就可以直接用一个int
        if (categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                //没有该分类，并且也没有关键字，这时候返回一个空的结果集，不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                //使分页生效，new一个PageInfo，传入productListVoList
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        //单独判断关键字
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();//拼接
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");//使用下划线进行分割
                PageHelper.orderBy(orderByArray[0] + "" + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return  ServerResponse.createBySuccess(pageInfo);
    }
}

package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

//处理后台商品分类的controller
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    private final IUserService iUserService;

    @Autowired
    public CategoryManageController(IUserService iUserService){
        this.iUserService = iUserService;
    }
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增加分类接口
     * @param session  参数需要传入session，校验用户是否是管理员
     * @param categoryName
     * @param parentId  品类的父节点
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody       //自动使用JSON序列化
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //如果isSuccess，就是管理员，然后增加处理分类的业务逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else{  //不是管理员，直接返回
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 更新CategoryName(品类名称)的接口
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //如果isSuccess为true，说明是管理员操作,更新categoryName

        }else{  //不是管理员，直接返回
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
        return null;
    }
}

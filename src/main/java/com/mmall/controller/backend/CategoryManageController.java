package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

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
     * @param parentId  品类的父结
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody       //使返回值自动使用JSON序列化
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //如果isSuccess，就是管理员，然后增加处理分类的业务逻辑
            //增加商品分类，参数：商品分类名称和父节点(父节点默认值为0)
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
    //更新分类的名称 update
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){  //参数：分类ID 分类名称  根据传入的分类ID选择性更新分类名称
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //如果isSuccess为true，说明是管理员操作,更新categoryName
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{  //不是管理员，直接返回
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 这个接口是根据传入的categoryId获取当前categoryId下的子节点的category信息，且子节点是平级的，不递归
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    //该接口是通过传入分类名称，查询该分类下的平级子节点的category信息，不采用递归
    public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session, //Parallel：平行的
                                                                      @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        //如果没有传入categoryId,那么它的默认值就为0，代表category的根节点
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){

            //查询子节点的category信息，不使用递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{  //不是管理员，直接返回
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    //服务器内部调用的接口，根据categoryId查询子节点的categoryId，递归查询
    /*其实这个接口主要是为了在商品搜索的时候，在服务端内部使用，也就是说查询某个分类id的时候，
    把这个商品所在所有的子品类id也要拿出来，然后进行便利查看。*/
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        //如果没有传入categoryId,那么它的默认值就为0，代表category的根节点
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //由于是服务器内部的接口，所以需要校验用户是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{  //不是管理员，直接返回
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}

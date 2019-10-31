package com.mmall.controller.backend;


import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    //保存商品的接口
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){//传入session判断登录的权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){ //校验用户是否是管理员
            //填充商品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //产品上下架接口，更新产品销售状态
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){//传入session判断登录的权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){ //校验用户是否是管理员
            //填充商品的业务逻辑
            return iProductService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //获取商品详情接口
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId){//传入session判断登录的权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){ //校验用户是否是管理员
            //填充业务逻辑
            return iProductService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //后台管理商品List
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){//传入session判断登录的权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){ //校验用户是否是管理员
            //填充业务逻辑
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //后台商品搜索功能
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){//传入session判断登录的权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){ //校验用户是否是管理员
            //填充业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file" ,required = false) MultipartFile file, HttpServletRequest request){//需要一个request参数，根据servlet的上下文动态的创建一个相对路径
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录管理员账号");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;//拼接url

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);

            return ServerResponse.createBySuccess(fileMap);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    //富文本中图片的上传
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtext_img_upload(HttpSession session, @RequestParam(value = "upload_file" ,required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {//需要一个request参数，根据servlet的上下文动态的创建一个相对路径
        // 富文本中对于返回值有自己的规范，使用simditor 按照simditor的要求进行返回
        /*{
              "success": true/false,
              "msg": "error message", # optional
              "file_path": "[real file path]"
            }*/
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断用户是否为空
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;//拼接url
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }

}

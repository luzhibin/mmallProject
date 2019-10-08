package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession httpSession){
        //调用service层-->调用dao层-->调用MyBatis
        ServerResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            httpSession.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /*退出登录功能*/
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /*注册接口*/
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /*校验username和email是否存在，虽然在注册时有校验，这样做为了防止用户恶意调用注册接口
     *当输入完用户名点击下一个输入框的时候，要实时调用一个校验接口，检查用户名和邮箱是否存在，并给予前台一个反馈,
     * */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str,type);
    }


    //获取用户登录信息的接口
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //空判断，如果user不等于null，就把他返回
        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }
    //忘记密码，密码提示问题
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    //校验问题答案是否正确
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    //返回token给前端，忘记密码后重置密码
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    //登录状态的重置密码
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        //先从session中获取用户，key值是常量CURRENT_USER
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    //更新用户个人信息
    //返回值为User对象，因为在更新完用户个人信息后，我们要把新的个人信息放到session里，同时要把信息返回给前端
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session, User user){
        //从session中获取用户，key值为常量，CURRENT_USER，代表当前用户
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        //判断用户是否登录
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //把UserId设置成当前登录的UserId
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    //获取用户的详细信息，如果用户调用这个接口时没有登录，就要对他进行强制登录
    public ServerResponse<User> get_information(HttpSession session){
        //从session中判断用户是否已经登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录status=10");
        }
        return  iUserService.getInformation(currentUser.getId());
    }
}
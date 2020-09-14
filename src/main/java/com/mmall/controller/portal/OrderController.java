package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by luzhibin on 2020/2/23 23:13
 */

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession httpSession,Integer shippingId){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession httpSession, Long orderNo, HttpServletRequest request){
        //HttpServletRequest 获取servletContext，获取upload文件夹，把自动生成的二维码上传到FTP服务器，返回给前端二维码图片地址，前端做二维码图片展示
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }

    //支付宝回调处理接口
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){

        Map<String,String> params = Maps.newHashMap();

        //支付宝的callback会把所有参数放到request里
        Map requestParams = request.getParameterMap();
        //迭代器
        for (Iterator iterator = requestParams.keySet().iterator();iterator.hasNext();){
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i=0;i<values.length;i++){
                //获取valueStr里的所有元素，如果有多个元素，则使用逗号分隔，然后拼接到一个字符串中
                valueStr = (i==values.length-1)?valueStr + values[i] : valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验签，验证回调的正确性，是否是支付宝的，并且还要避免重复通知

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params,Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if (!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据

        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    //前台轮询订单支付状态，二维码付款页面，前端调用该接口
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    //和前端约定的返回值为true或false，所以泛型为布尔
    public ServerResponse<Boolean> query_order_pay_status(HttpSession httpSession, Long orderNo){
        //HttpServletRequest 获取servletContext，获取upload文件夹，把自动生成的二维码上传到FTP服务器，返回给前端二维码图片地址，前端做二维码图片展示
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (serverResponse.isSuccess()){
            return serverResponse.createBySuccess(true);
        }
        return serverResponse.createBySuccess(false);
    }
}

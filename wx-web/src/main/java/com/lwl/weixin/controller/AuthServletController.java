package com.lwl.weixin.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lwl.weixin.authServlet.SNSUserInfo;
import com.lwl.weixin.authServlet.WeixinOauth2Token;
import com.lwl.weixin.authServlet.util.AdvancedUtil;
import com.lwl.weixin.util.SystemConfig;

@Controller
public class AuthServletController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * 如果用户没有关注微信号，调用此接口会弹出让用户授权的界面
	 * @param request
	 * @return
	 * @author lwlong
	 * @create 2017-8-23 下午2:42:18
	 */
	@RequestMapping("/authServlet")
	public ModelAndView authServlet(HttpServletRequest request){
		ModelAndView model = new ModelAndView("/index");
		model.addObject("title", "网页版授权，需要用户主动");
		  // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        SNSUserInfo snsUserInfo = new SNSUserInfo();
        
        logger.info("【AuthServletController：authServlet】的参数是code = "+code+" , state = "+state);
        // 用户同意授权
        if (code!=null&&!"authdeny".equals(code)) {
            // 获取网页授权access_token
            WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(SystemConfig.APP_ID, SystemConfig.APP_SECRET, code);
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2Token.getAccessToken();
            // 用户标识
            String openId = weixinOauth2Token.getOpenId();
            // 获取用户信息
             snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
        }
        // 设置要传递的参数
        model.addObject("user", snsUserInfo);
        model.addObject("state", state);
		return model;
	}
	
	/**
	 * 获取用户的基本信息
	 * 如果用户没有关注过微信号，调用此接口会有问题，需要对异常进行处理
	 * @param request
	 * @return
	 * @author lwlong  
	 * @create 2017-8-23 下午2:46:28
	 */
	@RequestMapping("/authServletNo")
	public ModelAndView authServletNo(HttpServletRequest request){
		ModelAndView model = new ModelAndView("/authNo");
		model.addObject("title", "网页版授权，不需要用户主动");
		SNSUserInfo snsUserInfo = new SNSUserInfo();
		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
		logger.info("【AuthServletController：authServletNo】的参数是code = "+code+" , state = "+state);
		// 用户同意授权
		if (code!=null&&!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(SystemConfig.APP_ID, SystemConfig.APP_SECRET, code);
			// 网页授权接口访问凭证
			String accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			 snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
		}
		 // 设置要传递的参数
        model.addObject("user", snsUserInfo);
        model.addObject("state", state);
		return model;
	}
	
	
}

package com.lwl.weixin.controller.pay;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lwl.weixin.pay.service.WXPay;
import com.lwl.weixin.pay.service.WXPrepay;
import com.lwl.weixin.pay.util.OrderUtil;
import com.lwl.weixin.util.SystemConfig;

@Controller
public class PayController {

	//通知回调地址
	String notifyUrl = "http://wxtest.zhicall.cn/wx-web/notice";
	
	private static final Logger logger = LoggerFactory.getLogger(PayController.class);
	

	/**
	 * 获取并填写支付参数 
	 * @param appId
	 * @param partnerId
	 * @param partnerKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/payIndex")
	public ModelAndView index(String openId) {
		ModelAndView model = new ModelAndView("/pay/payIndex");
		model.addObject("openId", openId);
		return model;
	}
	
	@RequestMapping("/payConfig")
	public String config(String openId,String fee, ModelMap model) {
		model.addAttribute("openId", openId);
		model.addAttribute("fee", fee);
		return "forward:wxpay";
	}
	
	/**
	 * 微信支付主方法
	 * @param request
	 * @param pay
	 * @param model
	 * @return
	 */
	@RequestMapping("/wxpay")
	public ModelAndView pay(HttpServletRequest request,String openId,String fee) {
		
		ModelAndView model = new ModelAndView("/pay/pay");
		
		String spbill_create_ip = request.getRemoteAddr();
		WXPrepay prePay = new WXPrepay();
		prePay.setAppid(SystemConfig.APP_ID);
		prePay.setBody("微信支付测试");
		prePay.setPartnerKey(SystemConfig.PARENER_KEY);
		prePay.setMch_id(SystemConfig.PARENER_ID);
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		prePay.setTotal_fee(fee);
		prePay.setTrade_type("JSAPI");
		prePay.setOpenid(openId);		//此处替换为OAuth2获取的openid
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！ 
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		logger.info("获取的预支付订单是：" + prepayid);
		if (prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			String jsParam = WXPay.createPackageValue(SystemConfig.APP_ID, SystemConfig.PARENER_KEY, prepayid);
			System.out.println("jsParam=" + jsParam);
			// 此处可以添加订单的处理逻辑
			model.addObject("jsParam", jsParam);
			logger.info("生成的微信调起JS参数为：" + jsParam);
		}else{
			logger.error("生成订单iD失败");
		}
		model.addObject("prepayid", prepayid);
		return model;
	}
	
	
}

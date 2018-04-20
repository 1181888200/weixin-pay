/**
 *通用微信入口controller
 */
package com.lwl.weixin.controller.home;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lwl.weixin.controller.BaseController;
import com.lwl.weixin.response.message.TextMessage;
import com.lwl.weixin.util.MessageUtil;
import com.lwl.weixin.util.SignUtil;

/**
 * 
 */
@Controller
@RequestMapping("/modelhome")
public class WeChatModelHomeController extends BaseController {

	
	/**
	 * 在开发者首次提交验证申请时，微信服务器将发送GET请求到填写的URL上，并且带上四个参数（signature、timestamp、nonce、
	 * echostr
	 * ），开发者通过对签名（即signature）的效验，来判断此条消息的真实性.此后，每次开发者接收用户消息的时候，微信也都会带上前面三个参数
	 * （signature、timestamp、nonce）访问开发者设置的URL，开发者依然通过对签名的效验判断此条消息的真实性
	 * 
	 * @param hospitalId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/{hospitalId}", method = RequestMethod.GET)
	public void index(@PathVariable long hospitalId, HttpServletRequest request, HttpServletResponse response) {
		logger.info("[WeChatModelHomeController.index]:start index.");
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		logger.info("[WeChatModelHomeController.index]:validate index,signature:" + signature + ",timestamp:"
				+ timestamp + ",nonce:" + nonce + ",echostr:" + echostr);

		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			writeAjaxJSONResponse(echostr, response);
		} else {
			writeAjaxJSONResponse(null, response);
		}

		logger.info("[WeChatModelHomeController.index]:end index,signature:" + signature + ",timestamp:" + timestamp
				+ ",nonce:" + nonce + ",echostr:" + echostr);
	}

	/**
	 * 处理事件
	 * 
	 * @param hospitalId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/{hospitalId}", method = RequestMethod.POST)
	public void executeCommand(@PathVariable String hospitalId, HttpServletRequest request, HttpServletResponse response) {
		logger.info("[WeChatModelHomeController.executeCommand]:start executeCommand.");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(
					"[WeChatModelHomeController.executeCommand]:request.setCharacterEncoding occured an UnsupportedEncodingException.",
					e);
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");

		String respMessage = "";
		try {
			Map<String, String> requestMap = MessageUtil.parseXML(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			logger.info("[WeChatModelHomeController.executeCommand]:fromUserName," + fromUserName + ",toUserName,"
					+ toUserName + ",msgType," + msgType);

			long hId = Long.parseLong(hospitalId);

			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 用户回复消息内容
				String content = requestMap.get("Content");
				respMessage = buildAlertMsg(hId,fromUserName, toUserName);
				logger.info("[WeChatModelHomeController.executeCommand]:input param is," + content);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				// 图片信息目前不能发送
				respMessage = buildAlertMsg(hId,fromUserName, toUserName);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respMessage = buildAlertMsg(hId,fromUserName, toUserName);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respMessage = buildAlertMsg(hId,fromUserName, toUserName);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respMessage = buildAlertMsg(hId,fromUserName, toUserName);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				String eventType = requestMap.get("Event");
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					// 订阅(hId, fromUserName, toUserName,0l);
					respMessage = buildAlertMsg(hId,fromUserName, toUserName);
					logger.error("[WeChatModelHomeController.executeCommand]:new user subscribe.");
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					logger.error("[WeChatModelHomeController.executeCommand]:old user unsubscribe.");
					// TODO 取消订阅
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					String className = "";
					String methodName = "";
					String eventKey = requestMap.get("EventKey");
					
					logger.info("[WeChatModelHomeController.executeCommand]:tigger click menu event,eventKey:"
							+ eventKey + ",className:" + className + ",methodName:" + methodName);
//					Class<?> myClassName = Class.forName(className);
//					WebApplicationContext webApplicationContext = WebApplicationContextUtils
//							.getWebApplicationContext(request.getSession().getServletContext());
//					Object o = webApplicationContext.getBean(myClassName);
//					//如果调用模版则调用模版反射
//					if(className.trim().equals("com.zhicall.weixin.callback.WeChatEventCommand_model")){
//						Method m1 = o.getClass().getMethod(methodName,
//								new Class[] { String.class, String.class, String.class,long.class });
//						respMessage = (String) m1.invoke(o, fromUserName, toUserName, hospitalId,menuId);
//					}else{
//						//调用自己原来的
//						Method m1 = o.getClass().getMethod(methodName,
//								new Class[] { String.class, String.class, String.class });
//						respMessage = (String) m1.invoke(o, fromUserName, toUserName, hospitalId);
//					}
					respMessage = buildAlertMsg(hId,fromUserName, toUserName);
				}
			}
		} catch (Exception e) {
			logger.info("[WeChatModelHomeController.executeCommand]:an exception occured,e:" + e);
			e.printStackTrace();
		}
		logger.info("[WeChatModelHomeController.executeCommand]:end executeCommand,respMessage is," + respMessage);
		writeAjaxJSONResponse(respMessage, response);
	}
	
	/**
	 * 构造提示消息
	 * 
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	private String buildAlertMsg(long hId,String fromUserName, String toUserName) {
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		String content = "感谢您对我们的关注!";
		textMessage.setContent(content);
		String result = MessageUtil.textMessageToXML(textMessage);
		logger.info("[WeChatModelHomeController.buildAlertMsg]:buildAlertMsg,result is," + result);
		return result;
	}
}
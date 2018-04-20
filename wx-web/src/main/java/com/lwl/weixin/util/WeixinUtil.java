/**
 * 公众平台通用接口工具类
 * 2014-07-23
 */
package com.lwl.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lwl.weixin.entity.menu.AccessToken;
import com.lwl.weixin.entity.menu.Menu;

/**
 * 
 */
public class WeixinUtil {

	private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
	
	public static Map<String, AccessToken> mapToken = new HashMap<String, AccessToken>();

	/**
	 * 获取接入token地址
	 */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 创建菜单地址
	 */
	public final static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		logger.info("[WeixinUtil.httpRequest]:start httpRequest.");
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		TrustManager[] tm = { new MyX509TrustManager() };
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url
					.openConnection();
			urlConnection.setSSLSocketFactory(ssf);

			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				;
			urlConnection.connect();

			if (null != outputStr) {
				OutputStream outputStream = urlConnection.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = urlConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream,
					"UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);

			String str = null;
			if ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			reader.close();
			inputStream.close();
			inputStream = null;

			urlConnection.disconnect();

			jsonObject = JSON.parseObject(buffer.toString());

		} catch (NoSuchAlgorithmException e) {
			logger.error("[WeixinUtil.httpRequest]:httpRequest occured an NoSuchAlgorithmException,NoSuchAlgorithmException:"
					+ e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			logger.error("[WeixinUtil.httpRequest]:httpRequest occured an NoSuchProviderException,NoSuchProviderException:"
					+ e);
			e.printStackTrace();
		} catch (KeyManagementException e) {
			logger.error("[WeixinUtil.httpRequest]:httpRequest occured an KeyManagementException,KeyManagementException:"
					+ e);
			e.printStackTrace();
		} catch (MalformedURLException e) {
			logger.error("[WeixinUtil.httpRequest]:httpRequest occured an MalformedURLException,MalformedURLException:"
					+ e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("[WeixinUtil.httpRequest]:httpRequest occured an IOException,IOException:"
					+ e);
			e.printStackTrace();
		}
		logger.info("[WeixinUtil.httpRequest]:end httpRequest.");
		return jsonObject;
	}

	/**
	 * 获取AccessToken
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		logger.info("[WeixinUtil.getAccessToken]:start getAccessToken,appid:"
				+ appid + ",appsecret:" + appsecret);
		
		AccessToken accessToken = mapToken.get(appid);
		if(accessToken!=null){
			//如果还在有效期内，则直接取出数据
			if(new Date().getTime()-accessToken.getExpiresIn()<=7000){
				return accessToken;
			}
		}
		
		String str = ACCESS_TOKEN_URL.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(str, "GET", null);

		if (jsonObject != null) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
				//存入map中
				mapToken.put(appid, accessToken);
			} catch (JSONException e) {
				accessToken = null;
				logger.error("[WeixinUtil.getAccessToken]:getAccessToken occured an JSONException,errcode:"
						+ jsonObject.getInteger("errcode")
						+ ",errmsg:"
						+ jsonObject.getString("errmsg")
						+ ",JSONException:"
						+ e);
			}
		}
		logger.info("[WeixinUtil.getAccessToken]:end getAccessToken.");
		return accessToken;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static int createMenu(Menu menu, String accessToken) {
		logger.info("[WeixinUtil.createMenu]:start createMenu.");
		int result = 0;
		String str = CREATE_MENU_URL.replace("ACCESS_TOKEN", accessToken);
		logger.info("[WeixinUtil.createMenu]:menu_url replace accessToken,url is:"
				+ str);
		String jsonMenu = JSONObject.toJSONString(menu);
		JSONObject jsonObject = httpRequest(str, "POST", jsonMenu);

		if (jsonObject != null) {
			if (jsonObject.getInteger("errcode") != 0) {
				result = jsonObject.getInteger("errcode");
				logger.error(
						"[WeixinUtil.createMenu]:create menu fail, errcode:{} errmsg:{}",
						jsonObject.getInteger("errcode"),
						jsonObject.getString("errmsg"));
			} else {
				logger.info("[WeixinUtil.createMenu]:create menu success.");
			}
		}
		logger.info("[WeixinUtil.createMenu]:end createMenu.");
		return result;
	}
	
	/**
	 * 微信错误码信息
	 * @return
	 */
	private static Map<String, String> getCodeName(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("-1","系统繁忙，此时请开发者稍候再试");
		map.put("0","请求成功");
		map.put("40001", "获取access_token时AppSecret错误，或者access_token无效。请开发者认真比对AppSecret的正确性，或查看是否正在为恰当的公众号调用接口");
		map.put("40002", "不合法的凭证类型");
		map.put("40003","不合法的OpenID，请开发者确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID" );
		map.put("40004","不合法的媒体文件类型" );
		map.put("40005", "不合法的文件类型");
		map.put("40006","不合法的文件大小" );
		map.put("40007","不合法的媒体文件id" );
		map.put("40008", "不合法的消息类型");
		map.put("40009", "不合法的图片文件大小");
		map.put("40010","不合法的语音文件大小" );
		map.put("40011", "不合法的视频文件大小");
		map.put("40012", "不合法的缩略图文件大小");
		map.put("40013","不合法的AppID，请开发者检查AppID的正确性，避免异常字符，注意大小写" );
		map.put("40014", "不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口");
		map.put("40015", "不合法的菜单类型");
		map.put("40016", "不合法的按钮个数");
		map.put("40017","不合法的按钮个数" );
		map.put("40018", "不合法的按钮名字长度");
		map.put("40019", "不合法的按钮KEY长度");
		map.put("40020","不合法的按钮URL长度" );
		map.put("40021","不合法的菜单版本号" );
		
		map.put("40022", "不合法的子菜单级数");
		map.put("40023", "不合法的子菜单按钮个数");
		map.put("40024", "不合法的子菜单按钮类型");
		map.put("40025", "不合法的子菜单按钮名字长度");
		map.put("40026", "不合法的子菜单按钮KEY长度");
		map.put("40027", "不合法的子菜单按钮URL长度");
		map.put("40028", "不合法的自定义菜单使用用户");
		map.put("40029", "不合法的oauth_code");
		map.put("40030", "不合法的refresh_token");
		map.put("40031", "不合法的openid列表");
		map.put("40032", "不合法的openid列表长度");
		map.put("40033", "不合法的请求字符，不能包含\\uxxxx格式的字符");
		map.put("40035", "不合法的参数");
		
		map.put("40038", "不合法的请求格式");
		map.put("40039", "不合法的URL长度");
		map.put("40050", "不合法的分组id");
		map.put("40051", "分组名字不合法");
		map.put("40117", "分组名字不合法");
		map.put("40118", "media_id大小不合法");
		map.put("40119", "button类型错误");
		map.put("40120", "button类型错误");
		map.put("40121", "不合法的media_id类型");
		map.put("41001", "缺少access_token参数");
		map.put("41002", "缺少appid参数");
		map.put("41003", "缺少refresh_token参数");
		map.put("41004", "缺少secret参数");
		map.put("41005", "缺少多媒体文件数据");
		map.put("41006", "缺少media_id参数");
		map.put("41007", "缺少子菜单数据");
		map.put("41008", "缺少oauth code");
		map.put("41009", "缺少openid");
		map.put("42001", "access_token超时，请检查access_token的有效期，请参考基础支持-获取access_token中，对access_token的详细机制说明");
		map.put("42002", "refresh_token超时");
		map.put("42003", "oauth_code超时");
		map.put("43001", "需要GET请求");
		map.put("43002", "需要POST请求");
		map.put("43003", "需要HTTPS请求");
		map.put("43004", "需要接收者关注");
		map.put("43005", "需要好友关系");
		map.put("44001", "多媒体文件为空");
		map.put("44002", "POST的数据包为空");
		map.put("44003", "图文消息内容为空");
		map.put("44004", "文本消息内容为空");
		map.put("45001", "多媒体文件大小超过限制");
		map.put("45002", "消息内容超过限制");
		map.put("45003", "标题字段超过限制");
		map.put("45004", "描述字段超过限制");
		map.put("45005", "链接字段超过限制");
		
		map.put("45006", "图片链接字段超过限制");
		map.put("45007", "语音播放时间超过限制");
		map.put("45008", "图文消息超过限制");
		map.put("45009", "接口调用超过限制");
		map.put("45010", "创建菜单个数超过限制");
		map.put("45015", "回复时间超过限制");
		map.put("45016", "系统分组，不允许修改");
		map.put("45017", "分组名字过长");
		map.put("45018", "分组数量超过上限");
		map.put("46001", "不存在媒体数据");
		map.put("46002", "不存在的菜单版本");
		map.put("46003", "不存在的菜单数据");
		map.put("46004", "不存在的用户");
		map.put("47001", "解析JSON/XML内容错误");
		map.put("48001", "api功能未授权，请确认公众号已获得该接口，可以在公众平台官网-开发者中心页中查看接口权限");
		map.put("50001", "用户未授权该api");
		map.put("50002", "用户受限，可能是违规后接口被封禁");
		map.put("61451", "参数错误(invalid parameter)");
		map.put("61452", "无效客服账号(invalid kf_account)");
		map.put("61453", "客服帐号已存在(kf_account exsited)");
		map.put("61454", "客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length)");
		map.put("61455", "客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account)");
		map.put("61456", "客服帐号个数超过限制(10个客服账号)(kf_account count exceeded)");
		map.put("61457", "无效头像文件类型(invalid file type)");
		map.put("61450", "系统错误(system error)");
		map.put("61500", "日期格式错误");
		map.put("61501", "日期范围错误");
		map.put("9001001", "POST数据参数不合法");
		map.put("9001002", "远端服务不可用");
		map.put("9001003", "Ticket不合法");
		map.put("9001004", "获取摇周边用户信息失败");
		map.put("9001005", "获取商户信息失败");
		map.put("9001006", "获取OpenID失败");
		map.put("9001007", "上传文件缺失");
		map.put("9001008", "上传素材的文件类型不合法");
		map.put("9001009", "上传素材的文件尺寸不合法");
		map.put("9001010", "上传失败");
		map.put("9001020", "帐号不合法");
		map.put("9001021", "已有设备激活率低于50%，不能新增设备");
		map.put("9001022", "设备申请数不合法，必须为大于0的数字");
		map.put("9001023", "已存在审核中的设备ID申请");
		map.put("9001024", "一次查询设备ID数量不能超过50");
		map.put("9001025", "设备ID不合法");
		map.put("9001026", "页面ID不合法");
		map.put("9001027", "页面参数不合法");
		map.put("9001028", "一次删除页面ID数量不能超过10");
		map.put("9001029", "页面已应用在设备中，请先解除应用关系再删除");
		map.put("9001030", "一次查询页面ID数量不能超过50");
		map.put("9001031", "时间区间不合法");
		map.put("9001032", "保存设备与页面的绑定关系参数错误");
		map.put("9001033", "门店ID不合法");
		map.put("9001034", "设备备注信息过长");
		map.put("9001035", "设备申请参数不合法");
		map.put("9001036", "查询起始值begin不合法");
		return map;
	}
	
	/**
	 * 根据错误码 获取对应的错误信息
	 * @param code
	 * @return
	 */
	public static String getCode(String code){
		Map<String, String>  map = getCodeName();
		return map.get(code)==null?"未知错误，请百度搜索相关信息":map.get(code);
	}
	
	 /**
     * URL编码（utf-8）
     * 
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static void main(String[] args) {
		System.out.println(urlEncodeUTF8("https://wloveo.cn/wx-web/authServlet"));
	}
	
}
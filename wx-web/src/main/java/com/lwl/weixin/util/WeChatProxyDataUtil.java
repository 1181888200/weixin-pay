/**
 * 发送POST请求
 * 2014-07-23
 */
package com.lwl.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class WeChatProxyDataUtil {

	private final static Logger logger = LoggerFactory.getLogger("REMOTE_CALL_LOGGER");

	private static final String ENCODE_TYPE = "utf-8";

	/**
	 * 请使用此方法，调用post请求
	 * 
	 * @param url
	 */
	public static String exec(String url) {
		//logger.info("[WeChatProxyDataUtil.exec]:start exec,url is," + url);
		InputStream inputStream = null;
		BufferedReader br = null;
		int statusCode = 0;
		HttpClient client = new HttpClient();
		// 连接时间
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
		// 数据传输时间
		client.getHttpConnectionManager().getParams().setSoTimeout(15 * 1000);

		PostMethod postMethod = new PostMethod(url);
		// 设置参数编码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// 构造键值对参数
		NameValuePair[] data = {};
		// 把参数值放入postMethod中
		postMethod.setRequestBody(data);
		String response = null;
		// 执行
		try {
			statusCode = client.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("send post request failed: " + postMethod.getStatusLine());
				return null;
			}
			response = readResponse(postMethod.getResponseBodyAsStream());
			// 打印返回的信息
			logger.info("[WeChatProxyDataUtil.exec]:send post request end,response is:" + response);
			// 释放连接
			postMethod.releaseConnection();
		} catch (HttpException e) {
			logger.error("[WeChatProxyDataUtil.exec]:an HttpException occured at send post request,HttpException:" + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("[WeChatProxyDataUtil.exec]:an IOException occured at send post request,IOException:" + e);
			e.printStackTrace();
		} finally {
			// 释放连接
			if (null != postMethod) {
				postMethod.releaseConnection();
			}
			// 流关闭
			try {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
				logger.error(
						"[WeChatProxyDataUtil.executeMethod]:close InputStream or BufferedReader occured an IOException:",
						e);
			}
		}
		logger.info("[WeChatProxyDataUtil.exec]:end exec,status is," + statusCode);
		return response;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String executeMethod(String url, NameValuePair[] data) throws Exception {
		//logger.info("[WeChatProxyDataUtil.executeMethod]:start executeMethod,url is," + url + ",data is," + data.toString());
		InputStream inputStream = null;
		BufferedReader br = null;
		int statusCode = 0;
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, ENCODE_TYPE);
		// 连接时间
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
		// 数据传输时间
		client.getHttpConnectionManager().getParams().setSoTimeout(15 * 1000);

		PostMethod post = new PostMethod(url){
        	@Override
        	public String getRequestCharSet() {
        		return "UTF-8";
        	}
        };
		post.setRequestHeader("Content-type", "application/x-www-form-urlencoded; text/xml; charset=UTF-8");

		String response = null;
		try {
//			post.setQueryString(data);
			post.addParameters(data);
			statusCode = client.executeMethod(post);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("send post request failed: " + post.getStatusLine());
				return null;
			}
			response = readResponse(post.getResponseBodyAsStream());
			// 打印返回的信息
			logger.info("[WeChatProxyDataUtil.executeMethod]:send post request end ,response is:" + response);
			post.releaseConnection();// 释放连接
		} catch (Exception e) {
			logger.info("[WeChatProxyDataUtil.executeMethod]:send post request occured an Exception:" + e);
			throw e;
		} finally {
			// 释放连接
			if (null != post) {
				post.releaseConnection();
			}
			// 流关闭
			try {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
				logger.error(
						"[WeChatProxyDataUtil.executeMethod]:close InputStream or BufferedReader occured an IOException:",
						e);
			}
		}
		logger.info("[WeChatProxyDataUtil.executeMethod]:end executeMethod,status is," + statusCode);
		return response;
	}

	/**
	 * 解析响应
	 * 
	 * @param is
	 * @return
	 */
	private static String readResponse(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODE_TYPE));
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			logger.warn("io exception:", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.warn("fail to close the input stream:", e);
			}
		}

		return sb.toString();
	}
}
/**
 * 消息工具类
 * 2014-07-23
 */
package com.lwl.weixin.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lwl.weixin.request.message.ImageMessage;
import com.lwl.weixin.response.message.ArticleMessage;
import com.lwl.weixin.response.message.NewsMessage;
import com.lwl.weixin.response.message.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 */
public class MessageUtil {
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 解析微信发来的XML
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXML(HttpServletRequest request)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		InputStream inputStream = request.getInputStream();

		SAXReader reader = new SAXReader();

		Document document = reader.read(inputStream);

		Element root = document.getRootElement();

		List<Element> elementList = root.elements();

		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		inputStream.close();

		inputStream = null;

		return map;
	}

	public static String textMessageToXML(TextMessage message) {
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}

	public static String imageMessageToXML(ImageMessage message) {
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}

	public static String newsMessageToXML(NewsMessage message) {
		xstream.alias("xml", message.getClass());
		xstream.alias("item", new ArticleMessage().getClass());
		return xstream.toXML(message);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> parseMenuXML(String hospitalId) {
		InputStream inputStream = MessageUtil.class.getClassLoader()
				.getResourceAsStream("menu/" + hospitalId + "_menu.xml");
		SAXReader reader = new SAXReader();
		Document document = null;

		try {
			document = reader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Map<String, String> map = null;
		Element root = document.getRootElement();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Element> subElementList = null;
		List<Element> elementList = null;

		List<Element> rootElementList = root.elements();
		for (Element e : rootElementList) {
			subElementList = e.elements();
			for (Element e1 : subElementList) {
				if (e1.getName().equals("subButton")) {
					map = new HashMap<String, String>();
					elementList = e1.elements();
					for (Element e2 : elementList) {
						map.put(e2.getName(), e2.getText());
					}
					list.add(map);
				}
			}
		}

		return list;
	}

	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
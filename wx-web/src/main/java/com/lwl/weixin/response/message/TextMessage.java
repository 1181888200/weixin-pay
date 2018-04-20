/**
 * 文本消息
 * 2014-07-23
 */
package com.lwl.weixin.response.message;

/**
 * 
 */
public class TextMessage extends BaseMessage {

	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
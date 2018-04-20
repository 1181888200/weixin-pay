/**
 * 图片消息
 * 2014-07-23
 */
package com.lwl.weixin.request.message;

/**
 * 
 */
public class ImageMessage extends BaseMessage {

	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
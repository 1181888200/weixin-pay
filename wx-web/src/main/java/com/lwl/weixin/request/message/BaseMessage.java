/**
 * 请求消息基类
 * 2014-07-23
 */
package com.lwl.weixin.request.message;

/**
 * 
 */
public class BaseMessage {

	/**
	 * 开发者微信号
	 */
	private String ToUserName;

	/**
	 * 发送方账号
	 */
	private String FromUserName;

	/**
	 * 消息创建时间
	 */
	private long CreateTime;

	/**
	 * 消息类型
	 */
	private String MsgType;

	/**
	 * 消息Id
	 */
	private long MesId;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public long getMesId() {
		return MesId;
	}

	public void setMesId(long mesId) {
		MesId = mesId;
	}
}
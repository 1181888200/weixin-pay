package com.lwl.weixin.pay.model;
/**
 * 支付参数
 *
 */
public class MdlPay {
	//微信号
	private String appId;
	//商户id,如：100xxxxx
	private String partnerId;
	//商户密钥 如：c9f0954adfa6481a3fd3d7d76
	private String partnerKey;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPartnerKey() {
		return partnerKey;
	}
	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}
	
}
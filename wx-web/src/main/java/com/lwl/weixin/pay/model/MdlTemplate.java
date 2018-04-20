package com.lwl.weixin.pay.model;


import java.util.HashMap;
/**
 * 模版消息类
 *
 */
public class MdlTemplate {
	private String touser;
	private String template_id;
	private String url;
	private String topcolor;
	private HashMap<String, MdlTemplateValue> data = new HashMap<String, MdlTemplateValue>();

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public HashMap<String, MdlTemplateValue> getData() {
		return data;
	}

	public void setData(HashMap<String, MdlTemplateValue> data) {
		this.data = data;
	}

	public void addItem(String key, String value, String color) {
		MdlTemplateValue v = new MdlTemplateValue();
		v.setValue(value);
		v.setColor(color);
		data.put(key, v);
	}
}

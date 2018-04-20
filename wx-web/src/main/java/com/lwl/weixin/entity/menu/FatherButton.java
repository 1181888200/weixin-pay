/**
 * 系统菜单
 * 2014-07-23
 */
package com.lwl.weixin.entity.menu;

/**
 * 
 */
public class FatherButton extends Button {

	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
}
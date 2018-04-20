package com.lwl.weixin.controller.vo;

/**
 * 包装ajax请求时，后台传到前台的信息
 *
 */
public class ViewerResult {

	private boolean isSuccess = true;   //界面请求的操作是否成功
	
	private String errMessage;   //错误消息
	
	private String successMsg;   //操作成功后的信息
	
	private Object resultData;   //操作成功后返回用户请求的数据

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}
}
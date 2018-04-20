/**
 * BaseController
 * 2014-07-23
 */
package com.lwl.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 */
public abstract class BaseController {

	protected static final String ERROR_MSG_KEY = "errMsg";

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected PrintWriter getWriter(HttpServletResponse response) {
		if (response == null)
			return null;

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (Exception e) {
			logger.error("unknow exception", e);
		}

		return writer;
	}

	/**
	 * send the string message back
	 * 
	 * @param returnResult
	 * @param response
	 */
	protected void writeAjaxResponse(String returnResult, HttpServletResponse response) {
		PrintWriter writer = getWriter(response);
		if (writer == null || returnResult == null) {
			return;
		}
		try {
			writer.write(returnResult);
		} finally {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * description:send the ajax response back to the client side.
	 * 
	 * @param
	 * @param
	 * @param writer
	 */
	protected void writeAjaxJSONResponse(Object responseObj, PrintWriter writer) {
		if (writer == null || responseObj == null) {
			return;
		}
		try {
			writer.write(responseObj.toString());
			// writer.write(JSON.toJSONString(responseObj,SerializerFeature.DisableCircularReferenceDetect).replaceAll("\"",
			// ""));
		} finally {
			writer.flush();
			writer.close();
		}
	}

	protected void writeJSONResponse(Object responseObj, PrintWriter writer) {
		if (writer == null || responseObj == null) {
			return;
		}
		try {
			writer.write(JSON.toJSONString(responseObj, SerializerFeature.DisableCircularReferenceDetect));
		} finally {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * description:send the ajax response back to the client side (Date object
	 * will be formatted as per the given <code>dateFormat</code>).
	 * 
	 * @param responseObj
	 * @param writer
	 * @param dateFormat
	 */
	protected void writeAjaxJSONResponseWithDateFormat(Object responseObj, PrintWriter writer, String dateFormat) {
		if (writer == null || responseObj == null || dateFormat == null) {
			return;
		}

		try {
			writer.write(JSON.toJSONStringWithDateFormat(responseObj, dateFormat,
					SerializerFeature.DisableCircularReferenceDetect));
		} finally {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * description:send the ajax response back to the client side.
	 * 
	 * @param responseObj
	 * @param response
	 */
	protected void writeAjaxJSONResponse(Object responseObj, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																					// 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies.
		PrintWriter writer = getWriter(response);
		writeAjaxJSONResponse(responseObj, writer);
	}

	protected void writeJSONResponse(Object responseObj, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																					// 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies.
		PrintWriter writer = getWriter(response);
		writeJSONResponse(responseObj, writer);
	}

	/**
	 * description:send the ajax response back to the client side (Date object
	 * will be formatted as per the given <code>dateFormat</code>).
	 * 
	 * @param responseObj
	 * @param response
	 * @param dateFormat
	 */
	protected void writeAjaxJSONResponseWithDateFormat(Object responseObj, HttpServletResponse response,
			String dateFormat) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																					// 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // Proxies.
		PrintWriter writer = getWriter(response);
		if (dateFormat != null)
			writeAjaxJSONResponseWithDateFormat(responseObj, writer, dateFormat);
		else
			writeAjaxJSONResponse(responseObj, writer);
	}
	
	/**
	 * 以图片的格式传输
	 * @param response
	 * @param byteStream
	 */
	protected void writePicStream(HttpServletResponse response,byte[] byteStream){
        if(byteStream==null){
            return;
        }
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(byteStream);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
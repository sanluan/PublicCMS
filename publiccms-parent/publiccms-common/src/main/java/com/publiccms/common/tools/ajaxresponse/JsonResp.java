package com.publiccms.common.tools.ajaxresponse;

import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class JsonResp extends ModelMap {

	private static final long serialVersionUID = 1L;
	private static final String FAIL = "fail";
	private static final String SUCCESS = "success";

	public JsonResp() {
		super();
	}

	public JsonResp(Object attributeValue) {
		super(attributeValue);
	}

	public JsonResp(String attributeName, Object attributeValue) {
		super(attributeName, attributeValue);
	}

	public static JsonResp success() {
		return newResp(SUCCESS, null);
	}

	public static JsonResp success(String msg) {
		return newResp(SUCCESS, msg);
	}

	public static JsonResp newResp(String result, String msg) {
		JsonResp resp = new JsonResp("result", result);
		resp.addAttribute("msg", msg);
		return resp;
	}

	public static JsonResp fail(String msg) {
		return newResp(FAIL, msg);
	}

	public JsonResp setP3PHeader(HttpServletResponse response) {
		if (response != null) {
			response.setHeader("P3P", "CP=CAO PSA OUR");
		}
		return this;
	}

	public JsonResp addAttr(String attributeName, Object attributeValue) {
		addAttribute(attributeName, attributeValue);
		return this;
	}

	public JsonResp addAllAttrs(Collection<?> attributeValues) {
		addAllAttributes(attributeValues);
		return this;
	}

	public JsonResp addAttr(Object attributeValue) {
		addAttribute(attributeValue);
		return this;
	}

	public JsonResp addAllAttrs(Map<String, ?> attributes) {
		addAllAttributes(attributes);
		return this;
	}

}

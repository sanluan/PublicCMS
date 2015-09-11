package com.publiccms.common.tools;

import static com.sanluan.common.constants.CommonConstants.COOKIES_USER;
import static com.sanluan.common.constants.CommonConstants.SESSION_ADMIN;
import static com.sanluan.common.constants.CommonConstants.SESSION_USER;
import static com.sanluan.common.constants.CommonConstants.SESSION_USER_TIME;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.publiccms.entities.system.SystemUser;
import com.sanluan.common.tools.RequestUtils;

public class UserUtils extends RequestUtils {
	public static SystemUser getUserFromSession(HttpServletRequest request) {
		return (SystemUser) getSession(request).getAttribute(SESSION_USER);
	}

	public static Date getUserTimeFromSession(HttpServletRequest request) {
		return (Date) getSession(request).getAttribute(SESSION_USER_TIME);
	}

	public static void setUserToSession(HttpServletRequest request, SystemUser user) {
		getSession(request).setAttribute(SESSION_USER, user);
		getSession(request).setAttribute(SESSION_USER_TIME, new Date());
	}

	public static void setUserToAttribute(HttpServletRequest request, SystemUser user) {
		request.setAttribute(SESSION_USER, user);
	}

	public static SystemUser getUserFromAttribute(HttpServletRequest request) {
		return (SystemUser) request.getAttribute(SESSION_USER);
	}

	public static void clearUserToSession(HttpServletRequest request, HttpServletResponse response) {
		cancleCookie(request, response, COOKIES_USER, null);
		getSession(request).removeAttribute(SESSION_USER);
	}

	public static void clearUserTimeToSession(HttpServletRequest request) {
		getSession(request).removeAttribute(SESSION_USER_TIME);
	}

	public static SystemUser getAdminFromSession(HttpServletRequest request) {
		return (SystemUser) getSession(request).getAttribute(SESSION_ADMIN);
	}

	public static void setAdminToSession(HttpServletRequest request, SystemUser user) {
		getSession(request).setAttribute(SESSION_ADMIN, user);
	}

	public static void clearAdminToSession(HttpServletRequest request) {
		getSession(request).removeAttribute(SESSION_ADMIN);
	}
}

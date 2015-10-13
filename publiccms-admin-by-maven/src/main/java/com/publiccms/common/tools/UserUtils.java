package com.publiccms.common.tools;

import static com.sanluan.common.constants.CommonConstants.COOKIES_USER;
import static com.sanluan.common.constants.CommonConstants.SESSION_ADMIN;
import static com.sanluan.common.constants.CommonConstants.SESSION_USER;
import static com.sanluan.common.constants.CommonConstants.SESSION_USER_TIME;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.publiccms.entities.system.SystemUser;
import com.sanluan.common.tools.RequestUtils;

/**
 * 
 * UserUtils 
 *
 */
public class UserUtils extends RequestUtils {
    /**
     * @param session
     * @return
     */
    public static SystemUser getUserFromSession(HttpSession session) {
        return (SystemUser) session.getAttribute(SESSION_USER);
    }

    /**
     * @param session
     * @return
     */
    public static Date getUserTimeFromSession(HttpSession session) {
        return (Date) session.getAttribute(SESSION_USER_TIME);
    }

    /**
     * @param session
     * @param user
     */
    public static void setUserToSession(HttpSession session, SystemUser user) {
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_USER_TIME, new Date());
    }

    /**
     * @param session
     * @param user
     */
    public static void setUserToAttribute(HttpSession session, SystemUser user) {
        session.setAttribute(SESSION_USER, user);
    }

    /**
     * @param session
     * @return
     */
    public static SystemUser getUserFromAttribute(HttpSession session) {
        return (SystemUser) session.getAttribute(SESSION_USER);
    }

    /**
     * @param request
     * @param response
     */
    public static void clearUserToSession(HttpServletRequest request, HttpServletResponse response) {
        cancleCookie(request, response, COOKIES_USER, null);
        request.getSession().removeAttribute(SESSION_USER);
    }

    /**
     * @param session
     */
    public static void clearUserTimeToSession(HttpSession session) {
        session.removeAttribute(SESSION_USER_TIME);
    }

    /**
     * @param session
     * @return
     */
    public static SystemUser getAdminFromSession(HttpSession session) {
        return (SystemUser) session.getAttribute(SESSION_ADMIN);
    }

    /**
     * @param session
     * @param user
     */
    public static void setAdminToSession(HttpSession session, SystemUser user) {
        session.setAttribute(SESSION_ADMIN, user);
    }

    /**
     * @param session
     */
    public static void clearAdminToSession(HttpSession session) {
        session.removeAttribute(SESSION_ADMIN);
    }
}

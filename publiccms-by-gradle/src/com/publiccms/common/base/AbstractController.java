package com.publiccms.common.base;

import static com.publiccms.common.constants.CommonConstants.COOKIES_USER;
import static com.publiccms.common.constants.CommonConstants.SESSION_ADMIN;
import static com.publiccms.common.constants.CommonConstants.SESSION_USER;
import static com.publiccms.common.constants.CommonConstants.SESSION_USER_TIME;
import static com.sanluan.common.tools.RequestUtils.cancleCookie;
import static com.sanluan.common.tools.RequestUtils.getUserAgent;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.SiteComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

public abstract class AbstractController extends BaseController {
    protected static final String TEMPLATE_INDEX = "index";
    protected static final String TEMPLATE_DONE = "common/ajaxDone";
    protected static final String TEMPLATE_ERROR = "common/ajaxError";
    protected static final String MESSAGE = "message";
    protected static final String SUCCESS = "success";

    public static final Pattern MOBILE_PATTERN = Pattern.compile("^(13|14|15|17|18|)\\d{9}$");
    public static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z_]{1}[0-9A-Za-z_]{3,40}$");
    public static final Pattern NICKNAME_PATTERN = Pattern.compile("^[0-9A-Za-z_\u4E00-\uFA29\uE7C7-\uE7F3]{2,45}$");
    private static final String VALID_CHARS = "[^\\s\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+]+";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS
            + "(\\." + VALID_CHARS + ")*)");

    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    
    protected SysDomain getDomain(HttpServletRequest request) {
        return siteComponent.getDomain(request.getServerName(), request.getServerPort());
    }

    protected SysSite getSite(HttpServletRequest request) {
        return siteComponent.getSite(request.getServerName(), request.getServerPort());
    }

    protected static MappingJacksonValue getMappingJacksonValue(Object object, String callback) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(object);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * @param request
     * @return
     * @throws IllegalStateException
     */
    protected static DeviceType getDeviceType(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(getUserAgent(request)).getOperatingSystem().getDeviceType();
    }

    /**
     * @param session
     * @return
     */
    public static SysUser getUserFromSession(HttpSession session) {
        return (SysUser) session.getAttribute(SESSION_USER);
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
    public static void setUserToSession(HttpSession session, SysUser user) {
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_USER_TIME, getDate());
    }

    /**
     * @param request
     * @param response
     */
    public static void clearUserToSession(String contextPath, HttpSession session, HttpServletResponse response) {
        cancleCookie(contextPath, response, COOKIES_USER, null);
        session.removeAttribute(SESSION_USER);
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
    public static SysUser getAdminFromSession(HttpSession session) {
        return (SysUser) session.getAttribute(SESSION_ADMIN);
    }

    /**
     * @param session
     * @param user
     */
    public static void setAdminToSession(HttpSession session, SysUser user) {
        session.setAttribute(SESSION_ADMIN, user);
    }

    /**
     * @param value
     * @return
     */
    public static boolean virifyNotUserName(String value) {
        Matcher m = USERNAME_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    public static boolean virifyNotNickName(String value) {
        Matcher m = NICKNAME_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected static boolean virifyNotMobile(String value) {
        Matcher m = MOBILE_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean virifyNotEMail(String field, String value, Map<String, Object> model) {
        if (virifyNotEMail(value)) {
            model.put(ERROR, "verify.notEmail." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean virifyNotUserName(String field, String value, Map<String, Object> model) {
        if (virifyNotUserName(value)) {
            model.put(ERROR, "verify.notUserName." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean virifyNotNickName(String field, String value, Map<String, Object> model) {
        if (virifyNotNickName(value)) {
            model.put(ERROR, "verify.notNickName." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean virifyNotMobile(String field, String value, Map<String, Object> model) {
        if (virifyNotMobile(value)) {
            model.put(ERROR, "verify.notMobile." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    public static boolean virifyNotEMail(String value) {
        Matcher m = EMAIL_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    public static boolean virifyNotNumber(String value) {
        Matcher m = NUMBER_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean virifyNotEMailAndMobile(String field, String value, Map<String, Object> model) {
        if (virifyNotEMail(value) && virifyNotMobile(value)) {
            model.put(ERROR, "verify.notEmailAndMobile." + field);
            return true;
        }
        return false;
    }

    /**
     * @param session
     */
    public static void clearAdminToSession(HttpSession session) {
        session.removeAttribute(SESSION_ADMIN);
    }
}

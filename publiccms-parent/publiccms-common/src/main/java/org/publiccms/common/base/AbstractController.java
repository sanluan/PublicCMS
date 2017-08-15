package org.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.RequestUtils.cancleCookie;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.publiccms.common.constants.CommonConstants.getCookiesUser;
import static org.publiccms.common.constants.CommonConstants.getSessionAdmin;
import static org.publiccms.common.constants.CommonConstants.getSessionUser;
import static org.publiccms.common.constants.CommonConstants.getSessionUserTime;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.component.site.SiteComponent;
import org.publiccms.logic.service.log.LogOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.publiccms.common.base.Base;

/**
 *
 * AbstractController
 * 
 */
public abstract class AbstractController implements Base {
    protected static final String REDIRECT = REDIRECT_URL_PREFIX;
    protected static final String FORWARD = FORWARD_URL_PREFIX;
    protected static final String TEMPLATE_INDEX = "index";
    protected static final String TEMPLATE_DONE = "common/ajaxDone";
    protected static final String TEMPLATE_ERROR = "common/ajaxError";
    protected static final String MESSAGE = "message";
    protected static final String SUCCESS = "success";
    protected static final String ERROR = "error";
    protected static final String ERROR_PAGE = "error.html";
    protected static MediaType jsonMediaType = new MediaType("application", "json", DEFAULT_CHARSET);
    protected final Log log = getLog(getClass());
    /**
     * Telephone Pattern
     */
    public static final Pattern TELPHONE_PATTERN = Pattern.compile("^\\+?\\d+([- ]?\\d+)?$");
    /**
     * Number Pattern
     */
    public static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    /**
     * UserName Pattern
     */
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z_]{1}[0-9A-Za-z_]{3,40}$");
    /**
     * NickName Pattern
     */
    public static final Pattern NICKNAME_PATTERN = Pattern.compile("^[0-9A-Za-z_\u4E00-\uFA29\uE7C7-\uE7F3]{2,45}$");
    private static final String VALID_CHARS = "[^\\s\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+]+";
    /**
     * Email Pattern
     */
    public static final Pattern EMAIL_PATTERN = Pattern
            .compile("(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS + "(\\." + VALID_CHARS + ")*)");

    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    protected SysDomain getDomain(HttpServletRequest request) {
        return siteComponent.getDomain(request.getServerName());
    }

    protected SysSite getSite(HttpServletRequest request) {
        return siteComponent.getSite(request.getServerName());
    }

    /**
     * @param session
     * @return SysUser
     */
    public static SysUser getUserFromSession(HttpSession session) {
        return (SysUser) session.getAttribute(getSessionUser());
    }

    /**
     * @param session
     * @return Date
     */
    public static Date getUserTimeFromSession(HttpSession session) {
        return (Date) session.getAttribute(getSessionUserTime());
    }

    /**
     * @param session
     * @param user
     */
    public static void setUserToSession(HttpSession session, SysUser user) {
        session.setAttribute(getSessionUser(), user);
        session.setAttribute(getSessionUserTime(), getDate());
    }

    /**
     * @param contextPath
     * @param session
     * @param response
     */
    public static void clearUserToSession(String contextPath, HttpSession session, HttpServletResponse response) {
        cancleCookie(contextPath, response, getCookiesUser(), null);
        session.removeAttribute(getSessionUser());
    }

    /**
     * @param session
     */
    public static void clearUserTimeToSession(HttpSession session) {
        session.removeAttribute(getSessionUserTime());
    }

    /**
     * @param session
     * @return SysUser
     */
    public static SysUser getAdminFromSession(HttpSession session) {
        return (SysUser) session.getAttribute(getSessionAdmin());
    }

    /**
     * @param session
     * @param user
     */
    public static void setAdminToSession(HttpSession session, SysUser user) {
        session.setAttribute(getSessionAdmin(), user);
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotUserName(String value) {
        Matcher m = USERNAME_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotNickName(String value) {
        Matcher m = NICKNAME_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    protected static boolean verifyNotTelphone(String value) {
        Matcher m = TELPHONE_PATTERN.matcher(value);
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
    protected static boolean verifyNotEMail(String field, String value, Map<String, Object> model) {
        if (verifyNotEMail(value)) {
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
    protected static boolean verifyNotUserName(String field, String value, Map<String, Object> model) {
        if (verifyNotUserName(value)) {
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
    protected static boolean verifyNotNickName(String field, String value, Map<String, Object> model) {
        if (verifyNotNickName(value)) {
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
    protected static boolean verifyNotTelphone(String field, String value, Map<String, Object> model) {
        if (verifyNotTelphone(value)) {
            model.put(ERROR, "verify.notTelphone." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotEMail(String value) {
        Matcher m = EMAIL_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotNumber(String value) {
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
    protected static boolean verifyNotEMailAndMobile(String field, String value, Map<String, Object> model) {
        if (verifyNotEMail(value) && verifyNotTelphone(value)) {
            model.put(ERROR, "verify.notEmailAndTelphone." + field);
            return true;
        }
        return false;
    }

    /**
     * @param session
     */
    public static void clearAdminToSession(HttpSession session) {
        session.removeAttribute(getSessionAdmin());
    }
}

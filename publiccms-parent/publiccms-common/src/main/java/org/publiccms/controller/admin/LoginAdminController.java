package org.publiccms.controller.admin;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyNotExist;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.publiccms.common.constants.CommonConstants.getDefaultPage;
import static org.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB_MANAGER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogLogin;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.component.cache.CacheComponent;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 *
 * LoginAdminController
 *
 */
@Controller
public class LoginAdminController extends AbstractController {

    @Autowired
    private SysUserService service;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private CacheComponent cacheComponent;

    /**
     * @param username
     * @param password
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        username = trim(username);
        password = trim(password);
        if (verifyNotEmpty("username", username, model) || verifyNotEmpty("password", password, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            return "login";
        }
        String ip = getIpAddress(request);
        SysUser user = service.findByName(site.getId(), username);
        if (verifyNotExist("username", user, model) || verifyNotEquals("password", encode(password), user.getPassword(), model)
                || verifyNotAdmin(user, model) || verifyNotEnablie(user, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            Long userId = null;
            if (null != user) {
                userId = user.getId();
            }
            logLoginService
                    .save(new LogLogin(site.getId(), username, userId, ip, CHANNEL_WEB_MANAGER, false, getDate(), password));
            return "login";
        }

        setAdminToSession(session, user);
        service.updateLoginStatus(user.getId(), getIpAddress(request));
        logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, CHANNEL_WEB_MANAGER, true, getDate(), null));

        if (notEmpty(returnUrl)) {
            return REDIRECT + returnUrl;
        }
        return REDIRECT + getDefaultPage();
    }

    /**
     * @param username
     * @param password
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "loginDialog", method = RequestMethod.POST)
    public String loginDialog(String username, String password, HttpServletRequest request, HttpSession session, ModelMap model) {
        if ("login".equals(login(username, password, null, request, session, model))) {
            return TEMPLATE_ERROR;
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param oldpassword
     * @param password
     * @param repassword
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public String changeMyselfPassword(String oldpassword, String password, String repassword, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysUser user = service.getEntity(getAdminFromSession(session).getId());
        if (verifyNotEquals("siteId", site.getId(), user.getSiteId(), model)) {
            return TEMPLATE_ERROR;
        }
        String encodedOldPassword = encode(oldpassword);
        if (verifyNotEquals("password", user.getPassword(), encodedOldPassword, model)) {
            return TEMPLATE_ERROR;
        } else if (verifyNotEmpty("password", password, model) || verifyNotEquals("repassword", password, repassword, model)) {
            return TEMPLATE_ERROR;
        } else {
            clearAdminToSession(session);
            model.addAttribute(MESSAGE, "message.needReLogin");
        }
        service.updatePassword(user.getId(), encode(password));
        logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "changepassword",
                getIpAddress(request), getDate(), encodedOldPassword));
        return "common/ajaxTimeout";
    }

    /**
     * @param session
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        clearAdminToSession(session);
        return REDIRECT + getDefaultPage();
    }

    /**
     * @return
     */
    @RequestMapping(value = "clearCache")
    public String clearCache() {
        cacheComponent.clear();
        return TEMPLATE_DONE;
    }

    protected boolean verifyNotAdmin(SysUser user, ModelMap model) {
        if (!user.isDisabled() && !user.isSuperuserAccess()) {
            model.addAttribute(ERROR, "verify.user.notAdmin");
            return true;
        }
        return false;
    }

	protected boolean verifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

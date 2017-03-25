package com.publiccms.controller.web.sys;

import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.logic.component.config.EmailTemplateConfigComponent.CONFIG_EMAIL_PATH;
import static com.publiccms.logic.component.config.EmailTemplateConfigComponent.CONFIG_EMAIL_TITLE;
import static com.publiccms.logic.component.site.EmailComponent.CONFIG_CODE;
import static com.sanluan.common.tools.FreeMarkerUtils.generateStringByFile;
import static com.sanluan.common.tools.FreeMarkerUtils.generateStringByString;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import freemarker.template.TemplateException;

/**
 * 
 * UserController 用户逻辑处理
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends AbstractController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysEmailTokenService sysEmailTokenService;
    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param oldpassword
     * @param password
     * @param repassword
     * @param callback
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public void changePassword(String oldpassword, String password, String repassword, String returnUrl,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        SysUser user = getUserFromSession(session);
        if (null == user || verifyNotEmpty("password", password, model)
                || verifyNotEquals("repassword", password, repassword, model)
                || verifyNotEquals("password", user.getPassword(), encode(oldpassword), model)) {
            redirect(response, returnUrl);
        } else {
            Cookie userCookie = getCookie(request.getCookies(), getCookiesUser());
            if (null != userCookie && notEmpty(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(getCookiesUserSplit());
                    if (userData.length > 1) {
                        sysUserTokenService.delete(userData[1]);
                    }
                }
            }
            clearUserToSession(request.getContextPath(), session, response);
            service.updatePassword(user.getId(), encode(password));
            model.addAttribute(MESSAGE, SUCCESS);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "changepassword",
                    getIpAddress(request), getDate(), user.getPassword()));
            redirect(response, returnUrl);
        }
    }

    /**
     * @param email
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "saveEmail", method = RequestMethod.POST)
    public void saveEmail(String email, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE);
        String emailTitle = config.get(CONFIG_EMAIL_TITLE);
        String emailPath = config.get(CONFIG_EMAIL_PATH);
        if (verifyNotEmpty("email", email, model) || verifyNotEmpty("email.config", emailTitle, model)
                || verifyNotEmpty("email.config", emailPath, model) || verifyNotEMail("email", email, model)
                || verifyHasExist("email", service.findByEmail(site.getId(), email), model)) {
            redirect(response, returnUrl);
        } else {
            SysUser user = getUserFromSession(session);
            SysEmailToken sysEmailToken = new SysEmailToken();
            sysEmailToken.setUserId(user.getId());
            sysEmailToken.setAuthToken(UUID.randomUUID().toString());
            sysEmailToken.setEmail(email);
            sysEmailTokenService.save(sysEmailToken);
            try {
                Map<String, Object> emailModel = new HashMap<String, Object>();
                emailModel.put("user", user);
                emailModel.put("site", site);
                emailModel.put("email", email);
                emailModel.put("authToken", sysEmailToken.getAuthToken());
                if (emailComponent.sendHtml(site.getId(), email,
                        generateStringByString(emailTitle, templateComponent.getWebConfiguration(), emailModel),
                        generateStringByFile(emailPath, templateComponent.getWebConfiguration(), emailModel))) {
                    model.addAttribute(MESSAGE, "sendEmail.success");
                } else {
                    model.addAttribute(MESSAGE, "sendEmail.error");
                }
            } catch (IOException | TemplateException | MessagingException e) {
                model.addAttribute(ERROR, "sendEmail.error");
            }
            redirect(response, returnUrl);
        }
    }

    /**
     * @param code
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "verifyEmail", method = RequestMethod.POST)
    public void verifyEmail(String authToken, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (verifyNotEmpty("verifyEmail.authToken", authToken, model)
                || verifyNotExist("verifyEmail.sysEmailToken", sysEmailToken, model)) {
            redirect(response, returnUrl);
        } else {
            sysEmailTokenService.delete(sysEmailToken.getAuthToken());
            service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
            clearUserTimeToSession(session);
            model.addAttribute(MESSAGE, "verifyEmail.success");
            redirect(response, returnUrl);
        }
    }
}

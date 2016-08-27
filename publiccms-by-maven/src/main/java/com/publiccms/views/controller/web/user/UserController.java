package com.publiccms.views.controller.web.user;

import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.logic.component.EmailComponent.CONFIG_CODE;
import static com.publiccms.logic.component.config.EmailTemplateConfigComponent.CONFIG_EMAIL_PATH;
import static com.publiccms.logic.component.config.EmailTemplateConfigComponent.CONFIG_EMAIL_TITLE;
import static com.publiccms.logic.component.config.EmailTemplateConfigComponent.CONFIG_SUBCODE;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByFile;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByString;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.ConfigComponent;
import com.publiccms.logic.component.EmailComponent;
import com.publiccms.logic.component.TemplateComponent;
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
@ResponseBody
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
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "changePassword")
    public String changePassword(String oldpassword, String password, String repassword, String returnUrl,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysUser user = getUserFromSession(session);
        if (!verifyNotEmpty("password", password, model) && !verifyNotEquals("repassword", password, repassword, model)) {
            if (!verifyNotEquals("password", user.getPassword(), encode(oldpassword), model)) {
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
                model.addAttribute(MESSAGE, "needReLogin");
                if (notEmpty(user)) {
                    service.updatePassword(user.getId(), encode(password));
                    logOperateService.save(new LogOperate(getSite(request).getId(), user.getId(), LogLoginService.CHANNEL_WEB,
                            "changepassword", getIpAddress(request), getDate(), user.getPassword()));
                }
            }
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param email
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "saveEmail")
    public String saveEmail(String email, String returnUrl, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE, CONFIG_SUBCODE);
        String emailTitle = config.get(CONFIG_EMAIL_TITLE);
        String emailPath = config.get(CONFIG_EMAIL_PATH);
        if (!verifyNotEmpty("email", email, model) && !verifyNotEmpty("email.config", emailTitle, model)
                && !verifyNotEmpty("config", emailTitle, model) && !verifyNotEMail("email.config", emailPath, model)
                && verifyHasExist("email", service.findByEmail(site.getId(), email), model)) {
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
                        makeStringByString(emailTitle, templateComponent.getWebConfiguration(), emailModel),
                        makeStringByFile(emailPath, templateComponent.getWebConfiguration(), emailModel))) {
                    model.addAttribute(MESSAGE, "sendEmail.success");
                } else {
                    model.addAttribute(MESSAGE, "sendEmail.error");
                }
            } catch (IOException | TemplateException | MessagingException e) {
                model.addAttribute("error", "sendEmail.error");
            }
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param code
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("verifyEmail")
    @ResponseBody
    public String verifyEmail(String authToken, String returnUrl, HttpSession session, ModelMap model) {
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (verifyNotEmpty("verifyEmail.authToken", authToken, model)
                || verifyNotExist("verifyEmail.sysEmailToken", sysEmailToken, model)) {
            return REDIRECT + returnUrl;
        }
        sysEmailTokenService.delete(sysEmailToken.getAuthToken());
        service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
        clearUserTimeToSession(session);
        model.addAttribute(MESSAGE, "verifyEmail.success");
        return REDIRECT + returnUrl;
    }
}

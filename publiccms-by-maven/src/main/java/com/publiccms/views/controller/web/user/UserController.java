package com.publiccms.views.controller.web.user;

import static com.publiccms.common.constants.CommonConstants.COOKIES_USER;
import static com.publiccms.common.constants.CommonConstants.COOKIES_USER_SPLIT;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByFile;
import static com.sanluan.common.tools.LanguagesUtils.getMessage;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.publiccms.logic.component.MailComponent;
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
    private MailComponent mailComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysEmailTokenService sysEmailTokenService;

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
        if (!virifyNotEmpty("password", password, model) && !virifyNotEquals("repassword", password, repassword, model)) {
            if (!virifyNotEquals("password", user.getPassword(), encode(oldpassword), model)) {
                Cookie userCookie = getCookie(request.getCookies(), COOKIES_USER);
                if (null != userCookie && notEmpty(userCookie.getValue())) {
                    String value = userCookie.getValue();
                    if (null != value) {
                        String[] userData = value.split(COOKIES_USER_SPLIT);
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
        if (!virifyNotEmpty("email", email, model) && !virifyNotEMail("email", email, model)
                && virifyHasExist("email", service.findByEmail(site.getId(), email), model)) {
            SysUser user = getUserFromSession(session);
            SysEmailToken sysEmailToken = new SysEmailToken();
            sysEmailToken.setUserId(user.getId());
            sysEmailToken.setAuthToken(UUID.randomUUID().toString());
            sysEmailToken.setEmail(email);
            sysEmailTokenService.save(sysEmailToken);

            try {
                Map<String, Object> emailModel = new HashMap<String, Object>();
                emailModel.put("user", user);
                emailModel.put("email", email);
                emailModel.put("authToken", sysEmailToken.getAuthToken());
                mailComponent.sendHtml(
                        email,
                        getMessage(request, "email.register.title", user.getNickName()),
                        makeStringByFile(getMessage(request, "email.register.template"), templateComponent.getWebConfiguration(),
                                emailModel));
            } catch (IOException | TemplateException e) {
                model.addAttribute("error", "saveEmail.email.error");
            }
            model.addAttribute(MESSAGE, "saveEmail.success");
        }
        return REDIRECT + returnUrl;
    }
}

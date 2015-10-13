package com.publiccms.web.views.controller.user;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogEmailCheck;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.MailComponent;
import com.publiccms.logic.service.log.LogEmailCheckService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.LanguagesUtils;
import com.sanluan.common.tools.VerificationUtils;

import freemarker.template.TemplateException;

/**
 * 
 * UserController 用户逻辑处理
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    private final String passwordPage = "user/password";
    @Autowired
    private SystemUserService service;
    @Autowired
    private MailComponent mailComponent;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogEmailCheckService logEmailCheckService;

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
    @RequestMapping(value = { "changePassword" })
    public String changePassword(String oldpassword, String password, String repassword, HttpServletRequest request,
            HttpSession session, HttpServletResponse response, ModelMap model) {
        SystemUser user = UserUtils.getUserFromSession(session);
        if (virifyNotEmpty("password", password, model) || virifyNotEquals("repassword", password, repassword, model)) {
            return passwordPage;
        } else {
            if (virifyNotEquals("password", user.getPassword(), VerificationUtils.encode(oldpassword), model)) {
                return passwordPage;
            } else {
                UserUtils.clearUserToSession(request, response);
                model.addAttribute("message", "needReLogin");
            }

            if (notEmpty(user)) {
                service.updatePassword(user.getId(), VerificationUtils.encode(password));
            }
        }
        return REDIRECT + "../login.html";
    }

    /**
     * @param email
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = { "saveEmail" })
    public String saveEmail(String email, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyNotEmpty("email", email, model) || virifyNotEMail("email", email, model)
                || virifyHasExist("email", service.findByEmail(email), model)) {
            return "editEmail";
        }
        SystemUser user = UserUtils.getUserFromSession(session);

        String emailCheckCode = UUID.randomUUID().toString();
        LogEmailCheck emailCheckLog = new LogEmailCheck();
        emailCheckLog.setUserId(user.getId());
        emailCheckLog.setCode(emailCheckCode);
        emailCheckLog.setEmail(email);
        logEmailCheckService.save(emailCheckLog);

        ModelMap emailModel = new ModelMap();
        emailModel.put("tempUser", user);
        emailModel.put("email", email);
        emailModel.put("emailCheckCode", emailCheckCode);

        try {
            mailComponent.sendHtml(email, LanguagesUtils.getMessage(request, "email.register.title", user.getNickName()),
                    fileComponent.dealStringTemplate(LanguagesUtils.getMessage(request, "email.register.template"), emailModel));
        } catch (IOException e) {
            log.debug(e.getMessage());
        } catch (TemplateException e) {
            log.debug(e.getMessage());
        }
        model.addAttribute("message", "saveEmail.success");
        return REDIRECT + "index.html";
    }
}

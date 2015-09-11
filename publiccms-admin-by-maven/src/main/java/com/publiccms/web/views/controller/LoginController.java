package com.publiccms.web.views.controller;

import static com.sanluan.common.constants.CommonConstants.COOKIES_USER;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogEmailCheck;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.service.log.LogEmailCheckService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;
import com.sanluan.common.tools.VerificationUtils;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private SystemUserService service;
	@Autowired
	private LogLoginService logLoginService;
	@Autowired
	private LogEmailCheckService logEmailCheckService;

	@RequestMapping(value = { "login" }, method = RequestMethod.POST)
	public String login(String username, String password, String returnUrl, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (virifyNotEmpty("username", username, model) || virifyNotEmpty("password", password, model)) {
			model.addAttribute("username", username);
			model.addAttribute("returnUrl", returnUrl);
			return "login";
		}
		SystemUser user;
		if (virifyNotEMail(username)) {
			user = service.findByName(username);
		} else {
			user = service.findByEmail(username);
		}
		if (virifyNotExist("username", user, model)
				|| virifyNotEquals("password", VerificationUtils.encode(password), user.getPassword(), model)
				|| virifyNotEnablie(user, model)) {
			model.addAttribute("username", username);
			model.addAttribute("returnUrl", returnUrl);
			LogLogin log = new LogLogin();
			log.setName(username);
			log.setErrorPassword(password);
			log.setIp(RequestUtils.getIp(request));
			logLoginService.save(log);
			return "login";
		}
		UserUtils.setUserToSession(request, user);
		String authToken = UUID.randomUUID().toString();
		RequestUtils.addCookie(request, response, COOKIES_USER, authToken, Integer.MAX_VALUE, null);
		service.updateLoginStatus(user.getId(), username, authToken, RequestUtils.getIp(request));
		if (notEmpty(returnUrl)) {
			try {
				returnUrl = URLDecoder.decode(returnUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
			}
			return REDIRECT + returnUrl;
		} else
			return REDIRECT + "index.html";
	}

	/**
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = { "register" })
	public String register(SystemUser entity, String repassword, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (virifyNotEmpty("username", entity.getName(), model) || virifyNotEmpty("nickname", entity.getNickName(), model)
				|| virifyNotEmpty("password", entity.getPassword(), model)
				|| virifyNotUserName("username", entity.getName(), model)
				|| virifyNotNickName("nickname", entity.getNickName(), model)
				|| virifyNotEquals("repassword", entity.getPassword(), repassword, model)
				|| virifyHasExist("username", service.findByName(entity.getName()), model)
				|| virifyHasExist("nickname", service.findByNickName(entity.getNickName()), model)) {
			return "register";
		} else {
			entity.setPassword(VerificationUtils.encode(entity.getPassword()));
			entity.setLastLoginIp(RequestUtils.getIp(request));
			String authToken = UUID.randomUUID().toString();
			entity.setAuthToken(authToken);
			entity = service.save(entity);

			UserUtils.setUserToSession(request, entity);
			RequestUtils.addCookie(request, response, COOKIES_USER, authToken, Integer.MAX_VALUE, null);
		}
		return REDIRECT + "user/";
	}

	@RequestMapping(value = { "verifyEmail" })
	public String verifyEmail(String code, HttpServletRequest request, ModelMap model) {
		LogEmailCheck logEmailCheck = logEmailCheckService.findByCode(code);
		if (virifyNotEmpty("verifyEmail.code", code, model) || virifyNotExist("verifyEmail.logEmailCheck", logEmailCheck, model)) {
			return "login";
		}
		service.checked(logEmailCheck.getUserId(), logEmailCheck.getEmail());
		logEmailCheckService.checked(logEmailCheck.getId());
		UserUtils.clearUserTimeToSession(request);
		model.addAttribute("message", "verifyEmail.success");
		return REDIRECT + "user/";
	}

	@RequestMapping(value = { "logout" }, method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		UserUtils.clearUserToSession(request, response);
		return REDIRECT + "index.html";
	}

	protected boolean virifyNotEnablie(SystemUser user, ModelMap model) {
		if (user.isDisabled()) {
			model.addAttribute(ERROR, "verify.user.notEnablie");
			return true;
		}
		return false;
	}
}

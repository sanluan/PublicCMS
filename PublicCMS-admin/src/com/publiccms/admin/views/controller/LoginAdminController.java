package com.publiccms.admin.views.controller;

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
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.component.CacheComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;
import com.sanluan.common.tools.VerificationUtils;

@Controller
public class LoginAdminController extends BaseController {
	@Autowired
	private SystemUserService service;
	@Autowired
	private CacheComponent cacheComponent;
	@Autowired
	private LogLoginService logLoginService;
	@Autowired
	private LogOperateService logOperateService;

	@RequestMapping(value = { "login" }, method = RequestMethod.POST)
	public String login(String username, String password, String returnUrl, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (virifyNotEmpty("username", username, model) || virifyNotEmpty("password", password, model)) {
			model.addAttribute("username", username);
			model.addAttribute("returnUrl", returnUrl);
			return "login";
		}
		SystemUser user = service.findByName(username);
		if (virifyNotExist("username", user, model)
				|| virifyNotEquals("password", VerificationUtils.encode(password), user.getPassword(), model)
				|| virifyNotAdmin(user, model)) {
			model.addAttribute("username", username);
			model.addAttribute("returnUrl", returnUrl);
			LogLogin log = new LogLogin();
			log.setName(username);
			log.setErrorPassword(password);
			log.setIp(UserUtils.getIp(request));
			logLoginService.save(log);
			return "login";
		}
		UserUtils.setAdminToSession(request, user);

		String authToken = UUID.randomUUID().toString();
		UserUtils.addCookie(request, response, COOKIES_USER, authToken, Integer.MAX_VALUE, null);
		service.updateLoginStatus(user.getId(), username, authToken, UserUtils.getIp(request));
		if (notEmpty(returnUrl)) {
			try {
				returnUrl = URLDecoder.decode(returnUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
			}
			return REDIRECT + returnUrl;
		} else
			return REDIRECT + "index.html";
	}

	@RequestMapping(value = { "systemUser/changePassword" }, method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request, Integer id, String oldpassword, String password, String repassword,
			ModelMap model) {
		if (notEmpty(id)) {
			SystemUser user = service.getEntity(id);
			if (notEmpty(user)) {
				if (virifyNotEquals("password", user.getPassword(), VerificationUtils.encode(oldpassword), model)) {
					return "common/ajaxError";
				} else if (virifyNotEmpty("password", password, model)
						|| virifyNotEquals("repassword", password, repassword, model)) {
					return "common/ajaxError";
				}
				service.updatePassword(user.getId(), VerificationUtils.encode(password));
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "changepassword",
						RequestUtils.getIp(request), getDate(), user.getId() + ":" + user.getPassword()));
			}
		}
		return "common/ajaxDone";
	}

	@RequestMapping(value = { "changePassword" }, method = RequestMethod.POST)
	public String changeMyselfPassword(HttpServletRequest request, Integer id, String oldpassword, String password,
			String repassword, ModelMap model) {
		SystemUser user = UserUtils.getAdminFromSession(request);
		if (virifyNotEquals("password", user.getPassword(), VerificationUtils.encode(oldpassword), model)) {
			return "common/ajaxError";
		} else if (virifyNotEmpty("password", password, model) || virifyNotEquals("repassword", password, repassword, model)) {
			return "common/ajaxError";
		} else {
			UserUtils.clearAdminToSession(request);
			model.addAttribute("message", "message.needReLogin");
		}
		service.updatePassword(user.getId(), VerificationUtils.encode(password));
		logOperateService.save(new LogOperate(user.getId(), "changepassword", RequestUtils.getIp(request), getDate(), user
				.getPassword()));
		return "common/ajaxTimeout";
	}

	@RequestMapping(value = { "logout" }, method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		UserUtils.clearAdminToSession(request);
		return REDIRECT + "index.html";
	}

	protected boolean virifyNotAdmin(SystemUser user, ModelMap model) {
		if (!user.isDisabled() && !user.isSuperuserAccess()) {
			model.addAttribute(ERROR, "verify.user.notAdmin");
			return true;
		}
		return false;
	}

	@RequestMapping(value = { "clearTemplateCache" })
	public String clearTemplateCache(HttpServletRequest request) {
		cacheComponent.clearTemplateCache();
		return "common/ajaxDone";
	}
}

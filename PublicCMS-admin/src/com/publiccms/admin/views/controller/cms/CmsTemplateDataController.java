package com.publiccms.admin.views.controller.cms;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("templateData")
public class CmsTemplateDataController extends BaseController {
	@Autowired
	private FileComponent fileComponent;
	@Autowired
	private LogOperateService logOperateService;

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(String path, Long createDate, HttpServletRequest request) throws IllegalStateException, IOException {
		Map<String, Object> data = new HashMap<String, Object>();
		Enumeration<String> parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String paramterName = parameters.nextElement();
			data.put(paramterName, request.getParameter(paramterName));
		}
		data.remove("path");
		data.remove("callbackType");
		if (null == createDate) {
			synchronized (this) {
				data.put("createDate", System.currentTimeMillis());
			}
			fileComponent.saveData(path, data);
			if (notEmpty(path)) {
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "save.template.data",
						RequestUtils.getIp(request), getDate(), path));
			}
		} else {
			data.put("createDate", createDate);
			fileComponent.updateData(path, createDate, data);
			if (notEmpty(path)) {
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "update.template.data",
						RequestUtils.getIp(request), getDate(), path));
			}
		}
		return "common/ajaxDone";
	}

	@RequestMapping("delete")
	public String delete(String path, Long createDate, HttpServletRequest request) {
		try {
			fileComponent.deleteData(path, createDate);
			if (notEmpty(path)) {
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "delete.template.data",
						RequestUtils.getIp(request), getDate(), path));
			}
		} catch (IOException e) {
		}
		return "common/ajaxDone";
	}
}

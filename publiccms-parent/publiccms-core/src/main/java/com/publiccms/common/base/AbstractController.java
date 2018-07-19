package com.publiccms.common.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * AbstractController
 * 
 */
public abstract class AbstractController {
	protected final Log log = LogFactory.getLog(getClass());

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

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值

	}
}

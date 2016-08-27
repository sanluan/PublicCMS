package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysAuthorizedDirective extends AbstractTemplateDirective {

	@Override
	public void execute(RenderHandler handler) throws IOException, Exception {
		Integer[] roleIds = handler.getIntegerArray("roleIds");
		String url = handler.getString("url");
		String[] urls = handler.getStringArray("urls");
		if (notEmpty(roleIds)) {
			if (notEmpty(url) && (notEmpty(service.getEntitys(roleIds, new String[] { url }))
					|| sysRoleService.showAllMoudle(roleIds))) {
				handler.put("object", true).render();
			} else if (notEmpty(urls)) {
				Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
				if (sysRoleService.showAllMoudle(roleIds)) {
					for (String u : urls) {
						map.put(u, true);
					}
				} else {
					for (String u : urls) {
						map.put(u, false);
					}
					for (SysRoleAuthorized entity : service.getEntitys(roleIds, urls)) {
						map.put(entity.getUrl(), true);
					}
				}
				handler.put("map", map).render();
			}
		}
	}

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleAuthorizedService service;

}
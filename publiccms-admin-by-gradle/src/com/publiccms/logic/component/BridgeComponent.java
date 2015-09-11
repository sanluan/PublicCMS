package com.publiccms.logic.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.index.CmsContentBridge;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.sanluan.common.servlet.TaskAfterInitServlet;

@Component
public class BridgeComponent implements TaskAfterInitServlet {

	@Autowired
	private CmsContentAttributeService contentAttributeService;

	@Override
	public void exec() {
		CmsContentBridge.contentAttributeService = contentAttributeService;
	}
}

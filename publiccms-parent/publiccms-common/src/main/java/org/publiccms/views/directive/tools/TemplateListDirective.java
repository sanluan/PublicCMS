package org.publiccms.views.directive.tools;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.component.site.FileComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * TemplateListDirective
 * 
 */
@Component
public class TemplateListDirective extends AbstractTemplateDirective {

	@Override
	public void execute(RenderHandler handler) throws IOException, Exception {
		String path = handler.getString("path", SEPARATOR);
		handler.put("list", fileComponent.getFileList(siteComponent.getWebTemplateFilePath(getSite(handler), path)))
				.render();
	}

	@Override
	public boolean needAppToken() {
		return true;
	}

	@Autowired
	private FileComponent fileComponent;
}
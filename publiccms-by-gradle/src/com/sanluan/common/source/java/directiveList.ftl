package ${base}.${directivePack};

// Generated ${.now} by SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

<#include "../include_imports/service.ftl">

import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.handler.PageHandler;

@Component
public class ${entityName}ListDirective extends BaseDirective {

	@Override
	public void execute(RenderHandler handler) throws IOException, Exception {
		PageHandler page = service.getPage(<#include "../include_condition/directive.ftl">);
		handler.put("page", page).render();
	}

	@Autowired
	private ${entityName}Service service;

}
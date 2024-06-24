package ${base}.${directivePack};

// Generated ${.now?date} by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

<#include "../include_imports/service.ftl">

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

import jakarta.annotation.Resource;
import freemarker.template.TemplateException;

/**
 *
 * ${entityName}List${directiveSuffix}
 * 
 */
@Component
public class ${entityName}List${directiveSuffix} extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(<#include "../include_condition/directive.ftl">);
        handler.put("page", page).render();
    }

    @Resource
    private ${entityName}Service service;

}
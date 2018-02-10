package ${base}.${directivePack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

<#include "../include_imports/service.ftl">

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * ${entityName}List${directiveSuffix}
 * 
 */
@Component
public class ${entityName}List${directiveSuffix} extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(<#include "../include_condition/directive.ftl">);
        handler.put("page", page).render();
    }

    @Autowired
    private ${entityName}Service service;

}
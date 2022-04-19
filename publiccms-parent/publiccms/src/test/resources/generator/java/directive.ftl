package ${base}.${directivePack};

// Generated ${.now?date} by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/service.ftl">

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

import jakarta.annotation.Resource;

/**
 *
 * ${entityName}${directiveSuffix}
 * 
 */
@Component
public class ${entityName}${directiveSuffix} extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        ${idType!'Long'} id = handler.get${idType!'Long'}("id");
        if (CommonUtils.notEmpty(id)) {
            ${entityName} entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            ${idType!'Long'}[] ids = handler.get${idType!'Long'}Array("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<${entityName}> entityList = service.getEntitys(ids);
                Map<String, ${entityName}> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private ${entityName}Service service;

}

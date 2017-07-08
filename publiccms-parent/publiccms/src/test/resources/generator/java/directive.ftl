package ${base}.${directivePack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/service.ftl">

import org.publliccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * ${entityName}${directiveSuffix}
 * 
 */
@Component
public class ${entityName}${directiveSuffix} extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<${entityName}> entityList = service.getEntitys(ids);
                Map<String, ${entityName}> map = new LinkedHashMap<>();
                for (${entityName} entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private ${entityName}Service service;

}

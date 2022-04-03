package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.views.pojo.entities.CmsCategoryType;

/**
 *
 * CmsCategoryTypeDirective
 * 
 */
@Component
public class CmsCategoryTypeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsCategoryType entity = modelComponent.getCategoryTypeMap(site).get(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                Map<String, CmsCategoryType> typeMap = modelComponent.getCategoryTypeMap(site);
                Map<String, CmsCategoryType> map = new LinkedHashMap<>();
                for (String typeId : ids) {
                    map.put(typeId, typeMap.get(typeId));
                }
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private ModelComponent modelComponent;

}

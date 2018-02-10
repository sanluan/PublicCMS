package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 *
 * CmsModelDirective
 * 
 */
@Component
public class CmsModelDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsModel entity = modelComponent.getMap(site).get(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                Map<String, CmsModel> modelMap = modelComponent.getMap(site);
                Map<String, CmsModel> map = new LinkedHashMap<>();
                for (String modelId : ids) {
                    map.put(modelId, modelMap.get(modelId));
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private ModelComponent modelComponent;

}

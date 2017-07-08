package org.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.template.ModelComponent;
import org.publiccms.views.pojo.CmsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(id)) {
            CmsModel entity = modelComponent.getMap(site).get(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (notEmpty(ids)) {
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

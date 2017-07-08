package org.publiccms.views.directive.task;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.entities.cms.CmsCategory;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * PublishCategoryDirective
 * 
 */
@Component
public class PublishCategoryDirective extends AbstractTaskDirective {
    
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        Integer pageIndex = handler.getInteger("pageIndex");
        Integer totalPage = handler.getInteger("totalPage");
        SysSite site = getSite(handler);
        Map<String, Boolean> map = new LinkedHashMap<>();
        if (notEmpty(id)) {
            CmsCategory entity = service.getEntity(id);
            map.put(entity.getId().toString(), deal(site, entity, pageIndex, totalPage));
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                for (CmsCategory entity : entityList) {
                    map.put(entity.getId().toString(), deal(site, entity, pageIndex, totalPage));
                }
            }
        }
        handler.put("map", map).render();
    }

    private boolean deal(SysSite site, CmsCategory entity, Integer pageIndex, Integer totalPage) {
        if (null != entity) {
            return templateComponent.createCategoryFile(site, entity, pageIndex, totalPage);
        }
        return false;
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryService service;
    
}

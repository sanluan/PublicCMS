package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;

import freemarker.template.TemplateException;

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
        if (CommonUtils.notEmpty(id)) {
            CmsCategory entity = service.getEntity(id);
            try {
                boolean result = templateComponent.createCategoryFile(site, entity, pageIndex, totalPage);
                map.put(id.toString(), result);
            } catch (IOException | TemplateException e) {
                handler.getWriter().append(e.getMessage());
                map.put(id.toString(), false);
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                for (CmsCategory entity : entityList) {
                    try {
                        boolean result = templateComponent.createCategoryFile(site, entity, pageIndex, totalPage);
                        map.put(entity.getId().toString(), result);
                    } catch (IOException | TemplateException e) {
                        handler.getWriter().append(e.getMessage());
                        handler.getWriter().append("\n");
                        map.put(entity.getId().toString(), false);
                    }

                }
            }
        }
        handler.put("map", map).render();
    }

    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private CmsCategoryService service;

}

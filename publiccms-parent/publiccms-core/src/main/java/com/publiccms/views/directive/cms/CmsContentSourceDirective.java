package com.publiccms.views.directive.cms;

// Generated 2026-1-25 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsContentSource;
import com.publiccms.logic.service.cms.CmsContentSourceService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;

/**
 *
 * CmsContentSourceDirective
 * 
 */
@Component
public class CmsContentSourceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            CmsContentSource entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContentSource> entityList = service.getEntitys(ids);
                Map<String, CmsContentSource> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), null, ids,
                        null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsContentSourceService service;

}

package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentService;

import freemarker.template.TemplateException;

/**
 *
 * PublishContentDirective
 * 
 */
@Component
public class PublishContentDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        Map<String, Boolean> map = new LinkedHashMap<>();
        if (CommonUtils.notEmpty(id)) {
            try {
                map.put(id.toString(), templateComponent.createContentFile(site, service.getEntity(id), null, null));
            } catch (IOException | TemplateException e) {
                handler.getWriter().append(e.getMessage());
                map.put(id.toString(), false);
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                for (CmsContent entity : entityList) {
                    try {
                        map.put(entity.getId().toString(), templateComponent.createContentFile(site, entity, null, null));
                    } catch (IOException | TemplateException e) {
                        handler.getWriter().append(e.getMessage());
                        handler.getWriter().append("\n");
                        map.put(entity.getId().toString(), false);
                    }
                }
            } else {
                log.info("begin batch publish");
                service.batchWork(site.getId(), handler.getIntegerArray("categoryIds"), handler.getStringArray("modelIds"),
                        list -> {
                            for (CmsContent content : list) {
                                try {
                                    templateComponent.createContentFile(site, content, null, null);
                                } catch (IOException | TemplateException e) {
                                    try {
                                        handler.getWriter().append(e.getMessage());
                                        handler.getWriter().append("\n");
                                    } catch (IOException e1) {
                                    }
                                }
                            }
                            log.info("batch publish size : " + list.size());
                        }, PageHandler.MAX_PAGE_SIZE);
                log.info("complete batch publish");
            }
        }
        handler.put("map", map).render();
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsContentService service;

}

package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryService;

/**
 *
 * CmsCategoryDirective
 * 
 */
@Component
public class CmsCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        String code = handler.getString("code");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id) || CommonUtils.notEmpty(code)) {
            CmsCategory entity;
            if (CommonUtils.notEmpty(id)) {
                entity = service.getEntity(id);
            } else {
                entity = service.getEntityByCode(site.getId(), code);
            }
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    templateComponent.initCategoryUrl(site, entity);
                }
                handler.put("object", entity);
                if (handler.getBoolean("containsAttribute", false)) {
                    CmsCategoryAttribute attribute = attributeService.getEntity(id);
                    if (null != attribute) {
                        Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                        map.put("title", attribute.getTitle());
                        map.put("keywords", attribute.getKeywords());
                        map.put("description", attribute.getDescription());
                        handler.put("attribute", map);
                    }
                }
                handler.render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                Consumer<CmsCategory> consumer = null;
                if (absoluteURL) {
                    consumer = e -> {
                        templateComponent.initCategoryUrl(site, e);
                    };
                }
                Map<String, CmsCategory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsCategoryService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryAttributeService attributeService;
}

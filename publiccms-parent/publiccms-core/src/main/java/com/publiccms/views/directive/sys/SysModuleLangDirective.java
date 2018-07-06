package com.publiccms.views.directive.sys;

// Generated 2018-6-5 21:58:15 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModuleLang;
import com.publiccms.entities.sys.SysModuleLangId;
import com.publiccms.logic.service.sys.SysModuleLangService;

/**
 *
 * SysModuleLangDirective
 * 
 */
@Component
public class SysModuleLangDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String moduleId = handler.getString("moduleId");
        String lang = handler.getString("lang");
        if (CommonUtils.notEmpty(moduleId) && null != lang) {
            SysModuleLang entity = service.getEntity(new SysModuleLangId(moduleId, lang));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Autowired
    private SysModuleLangService service;

}

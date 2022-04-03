package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;

/**
 *
 * CmsDictionaryExcludeListDirective
 * 
 */
@Component
public class CmsDictionaryExcludeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<CmsDictionaryExclude> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        if (CommonUtils.notEmpty(dictionaryId) || CommonUtils.notEmpty(excludeDictionaryId)) {
            SysSite site = getSite(handler);
            list = service.getList(site.getId(), dictionaryId, excludeDictionaryId);
        } else {
            list = new ArrayList<>();
        }
        handler.put("list", list).render();
    }

    @Resource
    private CmsDictionaryExcludeService service;

}
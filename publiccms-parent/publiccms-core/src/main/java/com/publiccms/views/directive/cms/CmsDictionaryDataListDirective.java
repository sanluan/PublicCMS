package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

/**
 *
 * CmsDictionaryDataListDirective
 * 
 */
@Component
public class CmsDictionaryDataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<CmsDictionaryData> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            list = service.getList(site.getId(), dictionaryId);
        } else {
            list = new ArrayList<>();
        }
        handler.put("list", list).render();
    }

    @Autowired
    private CmsDictionaryDataService service;

}
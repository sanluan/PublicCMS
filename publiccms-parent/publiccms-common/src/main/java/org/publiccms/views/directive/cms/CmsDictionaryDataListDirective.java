package org.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsDictionaryData;
import org.publiccms.logic.service.cms.CmsDictionaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(handler.getLong("dictionaryId"))) {
            list = service.getList(handler.getLong("dictionaryId"));
        } else {
            list = new ArrayList<>();
        }
        handler.put("list", list).render();
    }

    @Autowired
    private CmsDictionaryDataService service;

}
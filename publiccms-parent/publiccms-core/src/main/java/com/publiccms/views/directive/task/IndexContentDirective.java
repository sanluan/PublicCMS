package com.publiccms.views.directive.task;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 *
 * IndexContentDirective
 * 
 */
@Component
public class IndexContentDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        Long[] ids = handler.getLongArray("ids");
        if (CommonUtils.notEmpty(ids)) {
            service.index(getSite(handler).getId(), ids);
        } else if (CommonUtils.notEmpty(id)) {
            service.index(getSite(handler).getId(), new Long[] { id });
        }
    }

    @Autowired
    private CmsContentService service;
    
}

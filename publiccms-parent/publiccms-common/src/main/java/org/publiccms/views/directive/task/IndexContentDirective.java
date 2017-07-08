package org.publiccms.views.directive.task;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;

import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.logic.service.cms.CmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(ids)) {
            service.index(getSite(handler).getId(), ids);
        } else if (notEmpty(id)) {
            service.index(getSite(handler).getId(), new Long[] { id });
        }
    }

    @Autowired
    private CmsContentService service;
    
}

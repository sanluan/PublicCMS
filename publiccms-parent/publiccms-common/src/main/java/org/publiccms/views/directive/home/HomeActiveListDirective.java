package org.publiccms.views.directive.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.home.HomeActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * HomeActiveListDirective
 * 
 */
@Component
public class HomeActiveListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long userId = handler.getLong("userId");
        Long[] userIds = handler.getLongArray("userIds");
        PageHandler page;
        if (notEmpty(userId) || notEmpty(userIds)) {
            page = service.getPage(handler.getString("itemType"), userId, userIds, handler.getInteger("pageIndex", 1),
                    handler.getInteger("count", 30));
        } else {
            page = new PageHandler(0, 0, 0, 0);
        }
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needUserToken() {
        return true;
    }

    @Autowired
    private HomeActiveService service;

}
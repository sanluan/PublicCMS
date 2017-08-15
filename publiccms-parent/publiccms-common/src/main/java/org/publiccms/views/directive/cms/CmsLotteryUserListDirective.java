package org.publiccms.views.directive.cms;

// Generated 2016-3-1 17:24:24 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.cms.CmsLotteryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsLotteryUserListDirective
 * 
 */
@Component
public class CmsLotteryUserListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("lotteryId"), handler.getLong("userId"), handler.getBoolean("winning"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsLotteryUserService service;

}
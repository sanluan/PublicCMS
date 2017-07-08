package org.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getMinuteDate;

import java.io.IOException;
import java.util.Date;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.cms.CmsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsContentListDirective
 * 
 */
@Component
public class CmsContentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date endPublishDate = handler.getDate("endPublishDate");
        Integer[] status = new Integer[] { CmsContentService.STATUS_NORMAL };
        Boolean disabled = false;
        Boolean emptyParent = true;
        if (handler.getBoolean("advanced", false)) {
            status = handler.getIntegerArray("status");
            disabled = handler.getBoolean("disabled", false);
            emptyParent = handler.getBoolean("emptyParent");
        } else {
            Date now = getMinuteDate();
            if (empty(endPublishDate) || endPublishDate.after(now)) {
                endPublishDate = now;
            }
        }
        PageHandler page = service.getPage(getSite(handler).getId(), status, handler.getInteger("categoryId"),
                handler.getBoolean("containChild"), handler.getIntegerArray("categoryIds"), disabled,
                handler.getStringArray("modelId"), handler.getLong("parentId"), emptyParent, handler.getBoolean("onlyUrl"),
                handler.getBoolean("hasImages"), handler.getBoolean("hasFiles"), handler.getString("title"),
                handler.getLong("userId"), handler.getLong("checkUserId"), handler.getDate("startPublishDate"), endPublishDate,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;

}
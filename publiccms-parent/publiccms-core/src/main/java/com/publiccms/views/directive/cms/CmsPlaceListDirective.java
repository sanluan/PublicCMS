package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;

/**
 *
 * CmsPlaceListDirective
 * 
 */
@Component
public class CmsPlaceListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        Date endPublishDate = handler.getDate("endPublishDate");
        Date expiryDate = null;
        String path = handler.getString("path");
        Boolean disabled = false;
        Integer[] status;
        if (handler.getBoolean("advanced", false)) {
            status = handler.getIntegerArray("status");
            disabled = handler.getBoolean("disabled", false);
        } else {
            status = CmsPlaceService.STATUS_NORMAL_ARRAY;
            Date now = CommonUtils.getMinuteDate();
            if (null == endPublishDate || endPublishDate.after(now)) {
                endPublishDate = now;
            }
            expiryDate = now;
        }
        if (CommonUtils.notEmpty(path)) {
            path = path.replace("//", CommonConstants.SEPARATOR);
        }
        PageHandler page = service.getPage(site.getId(), handler.getLong("userId"), path, handler.getString("itemType"),
                handler.getLong("itemId"), handler.getDate("startPublishDate"), endPublishDate, expiryDate, status, disabled,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsPlace> list = (List<CmsPlace>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            list.forEach(e -> {
                Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                if (null != clicks) {
                    e.setClicks(e.getClicks() + clicks);
                }
                if (absoluteURL) {
                    templateComponent.initPlaceCover(site, e);
                }
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsPlaceService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;

}
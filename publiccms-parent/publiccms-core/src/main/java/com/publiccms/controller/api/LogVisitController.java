package com.publiccms.controller.api;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogVisit;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.VisitComponent;

@Controller
@RequestMapping("visit")
public class LogVisitController {
    @Autowired
    private VisitComponent visitComponent;
    @Autowired
    private SiteComponent siteComponent;

    /**
     * @param sessionId
     * @param url
     * @param title
     * @param screenw
     * @param screenh
     * @param site
     * @param userAgent
     * @param referer
     * @param itemType
     * @param itemId
     * @param request
     */
    @RequestMapping("record")
    @ResponseBody
    public void record(String sessionId, String url, String title, Integer screenw,
            Integer screenh, @RequestHeader(value = "User-Agent", required = false) String userAgent, String referer,
            String itemType, String itemId, HttpServletRequest request) {
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        SysSite site = siteComponent.getSite(request.getServerName());
        LogVisit entity = new LogVisit(site.getId(), sessionId, date, (byte) now.get(Calendar.HOUR_OF_DAY),
                RequestUtils.getIpAddress(request), userAgent, url, title, screenw, screenh, referer, itemType, itemId, date);
        visitComponent.add(entity);
    }
}

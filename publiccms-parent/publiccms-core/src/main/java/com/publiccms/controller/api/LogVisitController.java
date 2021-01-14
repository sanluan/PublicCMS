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
     * @param site
     * @param userAgent
     * @param referer
     * @param sessionId
     * @param url
     * @param itemType
     * @param itemId
     * @param request
     */
    @RequestMapping("record")
    @ResponseBody
    public void record(@RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "Referer", required = false) String referer, String sessionId, String url, String itemType,
            String itemId, HttpServletRequest request) {
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        LogVisit entity = new LogVisit(siteComponent.getSite(request.getServerName()).getId(), sessionId, date,
                (byte) now.get(Calendar.HOUR_OF_DAY), RequestUtils.getIpAddress(request), url, date);
        visitComponent.add(entity);
    }
}

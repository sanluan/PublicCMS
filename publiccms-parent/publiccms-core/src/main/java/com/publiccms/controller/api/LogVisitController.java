package com.publiccms.controller.api;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.logic.component.site.VisitComponent;

@Controller
@RequestMapping("visit")
public class LogVisitController {
    @Resource
    private VisitComponent visitComponent;

    /**
     * @param site 
     * @param sessionId
     * @param url
     * @param title
     * @param screenw
     * @param screenh
     * @param userAgent
     * @param referer
     * @param itemType
     * @param itemId
     * @param request
     */
    @RequestMapping("record")
    @ResponseBody
    public void record(@RequestAttribute SysSite site, String sessionId, String url, String title, Integer screenw, Integer screenh,
            @RequestHeader(value = "User-Agent", required = false) String userAgent, String referer, String itemType,
            String itemId, HttpServletRequest request) {
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        VisitHistory entity = new VisitHistory(site.getId(), sessionId, date, (byte) now.get(Calendar.HOUR_OF_DAY),
                RequestUtils.getIpAddress(request), userAgent, url, title, screenw, screenh, referer, itemType, itemId, date);
        visitComponent.add(entity);
    }
}

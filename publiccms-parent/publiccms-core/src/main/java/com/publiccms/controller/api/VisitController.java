package com.publiccms.controller.api;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.logic.component.site.VisitComponent;

@Controller
@RequestMapping("visit")
public class VisitController {
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
    public void recordData(@RequestAttribute SysSite site, String sessionId, String url, String title, Integer screenw,
            Integer screenh, @RequestHeader(value = "User-Agent", required = false) String userAgent, String referer,
            String itemType, String itemId, HttpServletRequest request) {
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        Long userId = null;
        Cookie userCookie = RequestUtils.getCookie(request.getCookies(), CommonConstants.getCookiesUser());
        if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
            String[] userData = userCookie.getValue().split(CommonConstants.getCookiesUserSplit());
            if (userData.length > 1) {
                try {
                    userId = Long.parseLong(userData[0]);
                } catch (NumberFormatException e) {
                }
            }
        }
        VisitHistory entity = new VisitHistory(site.getId(), sessionId, date, (byte) now.get(Calendar.HOUR_OF_DAY), userId,
                RequestUtils.getIpAddress(request), userAgent, url, title, screenw, screenh, referer, itemType, itemId, date);
        visitComponent.add(entity);
    }
}

package com.publiccms.controller.web.trade;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeAddress;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.trade.TradeAddressService;

/**
 *
 * TradeAddressController
 * 
 */
@Controller
@RequestMapping("tradeAddress")
public class TradeAddressController {

    private String[] ignoreProperties = new String[] { "id", "userId", "siteId" };

    /**
     * @param site
     * @param user
     * @param entity
     * @param returnUrl 
     * @param request
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser user, TradeAddress entity, String returnUrl,
            HttpServletRequest request) {
        if (null != entity.getId()) {
            if (site.getId() == entity.getSiteId() && entity.getUserId() == user.getId()) {
                entity = service.update(entity.getId(), entity, ignoreProperties);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "update.tradeAddress", RequestUtils.getIpAddress(request), CommonUtils.now(),
                        JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                    "save.tradeAddress", RequestUtils.getIpAddress(request), CommonUtils.now(), JsonUtils.getString(entity)));
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param user
     * @param returnUrl 
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Long[] ids, String returnUrl,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            service.delete(site.getId(), ids, user.getId());
            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                    "delete.tradeAddress", RequestUtils.getIpAddress(request), CommonUtils.now(),
                    StringUtils.join(ids, Constants.COMMA)));
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    @Resource
    private TradeAddressService service;
    @Resource
    protected LogOperateService logOperateService;
}
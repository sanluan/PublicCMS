package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.cms.CmsLottery;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsLotteryService;
import com.publiccms.logic.service.cms.CmsLotteryUserService;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class LotteryStatusDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long lotteryId = handler.getLong("lotteryId");
        CmsLottery lottery = lotteryService.getEntity(lotteryId);
        SysSite site = getSite(handler);
        if (notEmpty(lottery) && site.getId() == lottery.getSiteId() && !lottery.isDisabled()) {
            if (lotteryUserService.getPage(lotteryId, user.getId(), true, null, null, null, null, null).getTotalCount() == 0) {
                handler.put("winning", false);
                PageHandler page = lotteryUserService.getPage(lotteryId, user.getId(), null, null, null, null, null, null);
                handler.put("lastCount", lottery.getLotteryCount() - page.getTotalCount());
            } else {
                handler.put("winning", true);
            }
        } else {
            handler.put("error", true);
        }
    }

    @Autowired
    private CmsLotteryUserService lotteryUserService;
    @Autowired
    private CmsLotteryService lotteryService;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
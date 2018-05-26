package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsLottery;
import com.publiccms.entities.cms.CmsLotteryUser;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsLotteryService;
import com.publiccms.logic.service.cms.CmsLotteryUserService;

/**
 *
 * LotteryDirective
 * 
 */
@Component
public class LotteryDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long lotteryId = handler.getLong("lotteryId");
        CmsLottery lottery = lotteryService.getEntity(lotteryId);
        SysSite site = getSite(handler);
        handler.put("result", "failure");
        if (null != lottery && site.getId() == lottery.getSiteId() && !lottery.isDisabled()) {
            if (lotteryUserService.getPage(lotteryId, user.getId(), true, null, null, null, null, null).getTotalCount() == 0) {
                if (lottery.getLotteryCount() - lotteryUserService
                        .getPage(lotteryId, user.getId(), null, null, null, null, null, null).getTotalCount() > 0) {
                    CmsLotteryUser entity = new CmsLotteryUser(lotteryId, user.getId(), false, false,
                            RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate());
                    entity.setUserId(user.getId());
                    if (lottery.getFractions() > CommonConstants.random.nextInt(lottery.getNumerator())
                            && lotteryService.updateLastGift(lotteryId) && lottery.getLotteryCount() > lotteryUserService
                                    .getPage(lotteryId, null, true, null, null, null, null, null).getTotalCount()) {
                        entity.setWinning(true);
                    }
                    lotteryUserService.save(entity);
                    handler.put("result", "success");
                    handler.put("winning", entity.isWinning());
                }
            }
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
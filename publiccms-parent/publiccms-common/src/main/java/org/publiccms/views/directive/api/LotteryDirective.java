package org.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.io.IOException;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.entities.cms.CmsLottery;
import org.publiccms.entities.cms.CmsLotteryUser;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.cms.CmsLotteryService;
import org.publiccms.logic.service.cms.CmsLotteryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(lottery) && site.getId() == lottery.getSiteId() && !lottery.isDisabled()) {
            if (lotteryUserService.getPage(lotteryId, user.getId(), true, null, null, null, null, null).getTotalCount() == 0) {
                if (lottery.getLotteryCount() - lotteryUserService
                        .getPage(lotteryId, user.getId(), null, null, null, null, null, null).getTotalCount() > 0) {
                    CmsLotteryUser entity = new CmsLotteryUser(lotteryId, user.getId(), false, false,
                            getIpAddress(handler.getRequest()), getDate());
                    entity.setUserId(user.getId());
                    if (lottery.getFractions() > r.nextInt(lottery.getNumerator()) && lottery.getLastGift() > 0
                            && lottery.getLotteryCount() > lotteryUserService
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
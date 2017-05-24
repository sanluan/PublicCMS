package org.publiccms.logic.service.cms;

// Generated 2016-3-1 17:24:24 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.publiccms.entities.cms.CmsLotteryUser;
import org.publiccms.logic.dao.cms.CmsLotteryUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsLotteryUserService
 * 
 */
@Service
@Transactional
public class CmsLotteryUserService extends BaseService<CmsLotteryUser> {

    /**
     * @param lotteryId
     * @param userId
     * @param winning
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long lotteryId, Long userId, Boolean winning, Date startCreateDate, Date endCreateDate,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(lotteryId, userId, winning, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsLotteryUserDao dao;

}
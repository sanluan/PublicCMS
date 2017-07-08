package org.publiccms.logic.service.cms;

// Generated 2016-3-3 17:46:07 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.publiccms.entities.cms.CmsVoteUser;
import org.publiccms.logic.dao.cms.CmsVoteUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsVoteUserService
 * 
 */
@Service
@Transactional
public class CmsVoteUserService extends BaseService<CmsVoteUser> {

    /**
     * @param lotteryId
     * @param userId
     * @param ip
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer lotteryId, Long userId, String ip, Date startCreateDate, Date endCreateDate,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(lotteryId, userId, ip, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsVoteUserDao dao;

}
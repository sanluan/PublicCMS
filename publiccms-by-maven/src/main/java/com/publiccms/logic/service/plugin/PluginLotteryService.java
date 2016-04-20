package com.publiccms.logic.service.plugin;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.plugin.PluginLottery;
import com.publiccms.logic.dao.plugin.PluginLotteryDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class PluginLotteryService extends BaseService<PluginLottery> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, startStartDate, endStartDate, startEndDate, endEndDate, disabled, orderField, orderType,
                pageIndex, pageSize);
    }

    @Autowired
    private PluginLotteryDao dao;
}
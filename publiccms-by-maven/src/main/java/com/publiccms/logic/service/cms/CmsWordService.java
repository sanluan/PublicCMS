package com.publiccms.logic.service.cms;

// Generated 2016-3-22 11:21:35 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsWord;
import com.publiccms.logic.dao.cms.CmsWordDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsWordService extends BaseService<CmsWord> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Boolean hidden, Date startCreateDate, Date endCreateDate, String name,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, hidden, startCreateDate, endCreateDate, name, orderField, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsWordDao dao;
}
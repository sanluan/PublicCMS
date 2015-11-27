package com.publiccms.logic.service.cms;

// Generated 2015-7-20 11:47:55 by SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsForm;
import com.publiccms.logic.dao.cms.CmsFormDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsFormService extends BaseService<CmsForm> {

    @Autowired
    private CmsFormDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(String title, Date startCreateDate, Date endCreateDate, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(title, startCreateDate, endCreateDate, disabled, orderField, orderType, pageIndex, pageSize);
    }
}
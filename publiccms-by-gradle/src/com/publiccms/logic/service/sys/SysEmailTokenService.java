package com.publiccms.logic.service.sys;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.logic.dao.sys.SysEmailTokenDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysEmailTokenService extends BaseService<SysEmailToken> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, pageIndex, pageSize);
    }

    public int delete(Date createDate) {
        return dao.delete(createDate);
    }
    
    @Autowired
    private SysEmailTokenDao dao;
}

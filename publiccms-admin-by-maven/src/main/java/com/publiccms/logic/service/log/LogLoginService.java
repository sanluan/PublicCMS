package com.publiccms.logic.service.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogLogin;
import com.publiccms.logic.dao.log.LogLoginDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogLoginService extends BaseService<LogLogin> {

    @Autowired
    private LogLoginDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean result, Integer userId, 
                String name, Date startCreateDate, Date endCreateDate, String ip, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(result, userId, 
                name, startCreateDate, endCreateDate, ip, 
                orderField, orderType, pageIndex, pageSize);
    }
    
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }
}

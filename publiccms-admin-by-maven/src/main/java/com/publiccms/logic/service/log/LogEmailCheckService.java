package com.publiccms.logic.service.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogEmailCheck;
import com.publiccms.logic.dao.log.LogEmailCheckDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogEmailCheckService extends BaseService<LogEmailCheck> {

    @Autowired
    private LogEmailCheckDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer userId, Date startCreateDate, Date endCreateDate, 
            Boolean checked, 
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, startCreateDate, endCreateDate, 
            checked, 
            orderField, orderType, pageIndex, pageSize);
    }
    
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }

    public LogEmailCheck findByCode(String code) {
        return dao.findByCode(code);
    }

    public void checked(Integer id) {
        LogEmailCheck entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setChecked(true);
        }
    }
}

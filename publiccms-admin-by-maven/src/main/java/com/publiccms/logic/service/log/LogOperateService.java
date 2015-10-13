package com.publiccms.logic.service.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.dao.log.LogOperateDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogOperateService extends BaseService<LogOperate> {
    public static final String OPERATE_UPLOADFILE = "uploadfile";

    @Autowired
    private LogOperateDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(String content, String operate, String ip, Integer userId, Date startCreateDate,
            Date endCreateDate, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(content, operate, ip, userId, startCreateDate, endCreateDate, orderField, orderType, pageIndex, pageSize);
    }
    
    public int delete(Date createDate) {
        return dao.delete(createDate);
    }
}

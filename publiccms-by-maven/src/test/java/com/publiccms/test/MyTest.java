package com.publiccms.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.service.log.LogOperateService;

import config.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
public class MyTest {
    @Autowired
    private LogOperateService logOperateService;

    @Test
    public void log() {
        LogOperate entity = new LogOperate(1, 0, "test", "127.0.0.1", new Date(), "测试数据1");
        logOperateService.save(entity);
    }
}
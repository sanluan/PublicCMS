package com.publiccms.views.directive.task;

import static org.apache.commons.lang3.time.DateUtils.addMonths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.log.LogEmailCheckService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ClearLogDirective extends BaseTemplateDirective {
    @Autowired
    private LogEmailCheckService logEmailCheckService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date date = handler.getDate("clearDate");
        if (null == date) {
            date = addMonths(new Date(), -3);
        }
        List<String> messageList = new ArrayList<String>();
        messageList.add("email check log:" + logEmailCheckService.delete(date));
        messageList.add("login log:" + logLoginService.delete(date));
        messageList.add("operate log:" + logOperateService.delete(date));
        messageList.add("task log:" + logTaskService.delete(date));
        handler.put("messageList", messageList).render();
    }
}

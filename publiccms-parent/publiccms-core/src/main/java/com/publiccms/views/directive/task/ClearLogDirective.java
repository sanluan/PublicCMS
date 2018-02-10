package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;

/**
 *
 * ClearLogDirective
 * 
 */
@Component
public class ClearLogDirective extends AbstractTaskDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date date = handler.getDate("clearDate");
        if (null == date) {
            date = DateUtils.addMonths(CommonUtils.getDate(), -3);
        }
        SysSite site = getSite(handler);
        Map<String, Integer> map = new HashMap<>();
        map.put("loginLog:", logLoginService.delete(site.getId(), date));
        map.put("operateLog:", logOperateService.delete(site.getId(), date));
        map.put("taskLog:", logTaskService.delete(site.getId(), date));
        handler.put("result", map).render();
    }

    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;
}

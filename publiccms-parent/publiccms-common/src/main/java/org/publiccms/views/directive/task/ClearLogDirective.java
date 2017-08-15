package org.publiccms.views.directive.task;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static org.apache.commons.lang3.time.DateUtils.addMonths;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.log.LogOperateService;
import org.publiccms.logic.service.log.LogTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (empty(date)) {
            date = addMonths(getDate(), -3);
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

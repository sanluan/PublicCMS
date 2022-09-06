package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import jakarta.annotation.Resource;
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
* clearLog 日志清理指令
* <p>
* 参数列表
* <ul>
* <li><code>clearDate</code> 起始发布日期,【2020-01-01 23:59:59】,【2020-01-01】,默认3个月前
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>result</code>map类型
* <li><code>result.loginLog</code> 登录日志删除数量
* <li><code>result.operateLog</code> 操作日志删除数量
* <li><code>result.taskLog</code> 任务计划日志删除数量
* </ul>
* 使用示例
* <p>
* &lt;@task.clearLog&gt;&lt;#list result as
* k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@task.clearLog&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/task/clearLog?appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
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
        map.put("loginLog", logLoginService.delete(site.getId(), date));
        map.put("operateLog", logOperateService.delete(site.getId(), date));
        map.put("taskLog", logTaskService.delete(site.getId(), date));
        handler.put("result", map).render();
    }

    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogOperateService logOperateService;
    @Resource
    private LogTaskService logTaskService;
}

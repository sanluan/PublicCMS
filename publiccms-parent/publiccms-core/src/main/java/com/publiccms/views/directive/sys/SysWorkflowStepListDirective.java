package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.sys.SysWorkflowStepService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * sysWorkflowStepListDirective
 * <p>流程步骤列表查询指令
 *
 * <p>参数列表
 * <ul>
 * <li><code>workflowId</code>:流程id
 * </ul>
 * <p>返回结果
 * <ul>
 * <li><code>list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysExtendField}
 * </ul>
 * <p>使用示例
 * <p>
 * &lt;@sys.extendFieldList deptId=1 pageSize=10&gt;&lt;#list list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.extendFieldList&gt;
 *
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/workflowStepList?workflowId=1', function(data){
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysWorkflowStepListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        handler.put("list", service.getList(handler.getInteger("workflowId"), handler.getInteger("sort"))).render();
    }

    @Resource
    private SysWorkflowStepService service;

}
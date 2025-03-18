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
 * <p lang="zh">
 * 流程步骤列表查询指令
 * <p lang="en">
 * workflow step list query directive
 * <p lang="ja">
 * ワークフローステップリストクエリ指令
 * 
 * <p lang="zh">
 * 参数列表
 * <p lang="en">
 * parameter list
 * <p lang="ja">
 * パラメータリスト
 * <ul>
 * <li lang="zh"><code>workflowId</code>:流程id
 * <li lang="en"><code>workflowId</code>:workflow id
 * <li lang="ja"><code>workflowId</code>:ワークフロー id
 * </ul>
 * <p lang="zh">
 * 返回结果
 * <p lang="en">
 * return result
 * <p lang="ja">
 * 戻り値
 * <ul>
 * <li lang="zh"><code>list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysExtendField}
 * <li lang="en"><code>list</code>:List type query result entity list
 * {@link com.publiccms.entities.sys.SysExtendField}
 * <li lang="ja"><code>page.list</code>:リスト型 クエリ結果エンティティリスト
 * {@link com.publiccms.entities.sys.SysExtendField}
 * </ul>
 * <p lang="zh">
 * 使用示例
 * <p lang="en">
 * usage example
 * <p lang="ja">
 * 使用例
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
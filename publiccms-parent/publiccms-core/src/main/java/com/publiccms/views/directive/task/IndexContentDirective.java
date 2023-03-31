package com.publiccms.views.directive.task;

import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.service.cms.CmsContentService;

import freemarker.template.TemplateException;

/**
 *
 * indexContent 重建内容索引指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:内容id,ids为空时有效
 * <li><code>ids</code>:多个内容id
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@task.indexContent id=1&gt;&lt;&lt;/@task.indexContent&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/task/indexContent?id=1&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class IndexContentDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        Long[] ids = handler.getLongArray("ids");
        if (CommonUtils.notEmpty(ids)) {
            service.index(getSite(handler).getId(), ids);
        } else if (CommonUtils.notEmpty(id)) {
            service.index(getSite(handler).getId(), new Long[] { id });
        }
    }

    @Resource
    private CmsContentService service;

}

package com.publiccms.views.directive.tools;

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * memory 内存监控指令
 * <p>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>freeMemory</code> 空闲内存
 * <li><code>totalMemory</code> 总内存
 * <li><code>maxMemory</code> 最大内存
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.memory&gt;${totalMemory}&lt;/@tools.memory&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/tools/memory?appToken=接口访问授权Token', function(data){    
 console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class MemoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Runtime runtime = Runtime.getRuntime();
        handler.put("freeMemory", runtime.freeMemory());
        handler.put("totalMemory", runtime.totalMemory());
        handler.put("maxMemory", runtime.maxMemory());
        handler.render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}

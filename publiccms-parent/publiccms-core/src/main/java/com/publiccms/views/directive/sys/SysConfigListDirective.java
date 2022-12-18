package com.publiccms.views.directive.sys;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Collection;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.views.pojo.entities.ConfigInfo;

/**
*
* sysConfigList 配置列表查询指令
* <p>
* <p>
* 返回结果
* <ul>
* <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.views.pojo.entities.ConfigInfo}
* </ul>
* 使用示例
* <p>
* &lt;@sys.configList&gt;&lt;#list list as
* a&gt;${a.code}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.configList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/configList?appToken=接口访问授权Token', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class SysConfigListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Collection<ConfigInfo> list = configComponent.getConfigList(getSite(handler).getId(), handler.getLocale(),
                getAdvanced(handler));
        handler.put("list", list).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private ConfigComponent configComponent;
}
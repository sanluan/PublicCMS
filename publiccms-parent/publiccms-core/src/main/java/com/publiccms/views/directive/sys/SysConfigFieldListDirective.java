package com.publiccms.views.directive.sys;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.config.ConfigComponent;

/**
 *
 * sysConfigFieldList 配置数据字段列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>code</code> 配置编码
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysExtendField}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.configFieldList pageSize=10&gt;&lt;#list list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.configFieldList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/configFieldList?appToken=接口访问授权Token', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysConfigFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        if (CommonUtils.notEmpty(code)) {
            handler.put("list",
                    configComponent.getFieldList(getSite(handler), code, handler.getBoolean("customed"), handler.getLocale()))
                    .render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private ConfigComponent configComponent;

}

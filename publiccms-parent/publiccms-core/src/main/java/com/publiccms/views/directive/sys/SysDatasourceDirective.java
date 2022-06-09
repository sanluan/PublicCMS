package com.publiccms.views.directive.sys;

// Generated 2021-8-2 11:31:34 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDatasource;
import com.publiccms.logic.service.sys.SysDatasourceService;

/**
*
* sysDatasource 数据源查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 数据源id，结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysDatasource}
* <li><code>ids</code>
* 多个数据源id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.datasource id=1&gt;${object.name}&lt;/@sys.datasource&gt;
* <p>
* &lt;@sys.datasource ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.datasource&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/sys/datasource?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysDatasourceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            SysDatasource entity = service.getEntity(id);
            if (null != entity) {
                Properties properties = new Properties();
                properties.load(new StringReader(entity.getConfig()));
                handler.put("object", entity).put("properties", properties).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDatasource> entityList = service.getEntitys(ids);
                Map<String, SysDatasource> map = CommonUtils.listToMap(entityList, k -> k.getName(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDatasourceService service;

}

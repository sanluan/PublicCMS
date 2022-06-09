package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysDeptService;

/**
*
* sysDept 部门查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 部门id，结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysDept}
* <li><code>ids</code>
* 多个部门id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.dept id=1&gt;${object.name}&lt;/@sys.dept&gt;
* <p>
* &lt;@sys.dept ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.dept&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/dept?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysDeptDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysDept entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDept> entityList = service.getEntitys(ids);
                Map<String, SysDept> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
    
    @Autowired
    private SysDeptService service;

}

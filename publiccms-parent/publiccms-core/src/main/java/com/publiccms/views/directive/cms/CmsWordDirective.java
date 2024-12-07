package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsWordService;

import freemarker.template.TemplateException;

/**
*
* word 搜索词查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:搜索词id,结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsWord}
* <li><code>ids</code>:
* 多个搜索词id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.word id=1&gt;${object.name}&lt;/@cms.word&gt;
* <p>
* &lt;@cms.word ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.word&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/word?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsWordDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsWord entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsWord> entityList = service.getEntitys(ids);
                Map<String, CmsWord> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), null, ids,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsWordService service;

}

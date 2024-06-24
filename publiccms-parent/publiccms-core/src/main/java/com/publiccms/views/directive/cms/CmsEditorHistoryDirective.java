package com.publiccms.views.directive.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;

import jakarta.annotation.Resource;
import freemarker.template.TemplateException;

/**
*
* editorHistory 正文历史查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:id,结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsEditorHistory}
* <li><code>ids</code>:
* 多个id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.editorHistory id=1&gt;${object.text}&lt;/@cms.editorHistory&gt;
* <p>
* &lt;@cms.editorHistory ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${k}:${v.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.editorHistory&gt;
* 
* <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/editorHistory?id=1&amp;appToken=接口访问授权Token', function(data){    
     console.log(data.text);
   });
   &lt;/script&gt;
* </pre>
*/
@Component
public class CmsEditorHistoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            CmsEditorHistory entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsEditorHistory> entityList = service.getEntitys(ids);
                Map<String, CmsEditorHistory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsEditorHistoryService service;

}

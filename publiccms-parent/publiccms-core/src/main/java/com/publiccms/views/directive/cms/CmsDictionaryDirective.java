package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryService;

/**
*
* dictionary 数据字典查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 值,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsDictionary} 
* <li><code>ids</code> 多个值,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.dictionary id='data'&gt;${object.name}&lt;/@cms.dictionary&gt;
* <p>
* &lt;@cms.dictionary values='data,data2'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionary&gt;
* 
* <pre>
*  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/cms/dictionary?dictionaryId=data&amp;value=1', function(data){    
     console.log(data.name);
   });
   &lt;/script&gt;
* </pre>
*/
@Component
public class CmsDictionaryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            CmsDictionaryId entityId = new CmsDictionaryId(id, site.getId());
            CmsDictionary entity = service.getEntity(entityId);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                CmsDictionaryId[] entityIds = new CmsDictionaryId[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    entityIds[i] = new CmsDictionaryId(ids[i], site.getId());
                }
                List<CmsDictionary> entityList = service.getEntitys(entityIds);
                Map<String, CmsDictionary> map = CommonUtils.listToMap(entityList, k -> k.getId().getId(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsDictionaryService service;

}

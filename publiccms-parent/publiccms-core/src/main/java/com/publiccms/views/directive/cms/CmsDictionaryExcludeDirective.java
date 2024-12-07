package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;

import freemarker.template.TemplateException;

/**
 *
 * dictionaryExclude 数据字典排除规则查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code>:数据字典id
 * <li><code>excludeDictionaryId</code>
 * 排除的字典,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsDictionaryExclude}
 * <li><code>excludeDictionaryIds</code>
 * 多个排除的字典,逗号或空格间隔,当excludeDictionaryId为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.dictionaryExclude dictionaryId='data'
 * excludeDictionaryId='data1'&gt;${object.id.excludeDictionaryId}&lt;/@cms.dictionaryExclude&gt;
 * <p>
 * &lt;@cms.dictionaryExclude dictionaryId='data'
 * excludeDictionaryIds='data1,data2'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.id.excludeDictionaryId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryExclude&gt;
 * 
 * <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/dictionaryExclude?dictionaryId=data&amp;value=1', function(data){    
     console.log(data.id.excludeDictionaryId);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryExcludeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            if (CommonUtils.notEmpty(excludeDictionaryId)) {
                CmsDictionaryExclude entity = service.getEntity(new CmsDictionaryExcludeId(dictionaryId, siteId, excludeDictionaryId));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] excludeDictionaryIds = handler.getStringArray("excludeDictionaryIds");
                if (CommonUtils.notEmpty(excludeDictionaryIds)) {
                    CmsDictionaryExcludeId[] ids = new CmsDictionaryExcludeId[excludeDictionaryIds.length];
                    for (int i = 0; i < excludeDictionaryIds.length; i++) {
                        ids[i] = new CmsDictionaryExcludeId(dictionaryId, siteId, excludeDictionaryIds[i]);
                    }
                    List<CmsDictionaryExclude> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryExclude> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().getExcludeDictionaryId(), excludeDictionaryIds);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsDictionaryExcludeService service;

}

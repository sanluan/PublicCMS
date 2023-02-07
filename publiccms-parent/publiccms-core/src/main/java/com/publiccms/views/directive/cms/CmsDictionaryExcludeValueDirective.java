package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.entities.cms.CmsDictionaryExcludeValueId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;

/**
 *
 * dictionaryExcludeValue 数据字典排除值查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code> 数据字典id
 * <li><code>excludeDictionaryId</code> 排除的字典
 * <li><code>value</code>
 * 字典值,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsDictionaryExcludeValue}
 * <li><code>values</code>
 * 多个的字典值,逗号或空格间隔,当value为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.dictionaryExcludeValue dictionaryId='data' excludeDictionaryId='data1'
 * value='data1'&gt;${object.excludeValues}&lt;/@cms.dictionaryExcludeValue&gt;
 * <p>
 * &lt;@cms.dictionaryExcludeValue dictionaryId='data' excludeDictionaryId='data1'
 * values='1,2'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.excludeValues}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryExcludeValue&gt;
 * 
 * <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/dictionaryExcludeValue?dictionaryId=data&amp;excludeDictionaryId=data1&amp;value=1', function(data){    
     console.log(data.excludeValues);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryExcludeValueDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        String value = handler.getString("value");
        if (CommonUtils.notEmpty(dictionaryId) && CommonUtils.notEmpty(excludeDictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            if (CommonUtils.notEmpty(value)) {
                CmsDictionaryExcludeValue entity = service
                        .getEntity(new CmsDictionaryExcludeValueId(dictionaryId, siteId, excludeDictionaryId, value));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] values = handler.getStringArray("values");
                if (CommonUtils.notEmpty(values)) {
                    CmsDictionaryExcludeValueId[] ids = new CmsDictionaryExcludeValueId[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ids[i] = new CmsDictionaryExcludeValueId(dictionaryId, siteId, excludeDictionaryId, values[i]);
                    }
                    List<CmsDictionaryExcludeValue> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryExcludeValue> map = CommonUtils.listToMap(entityList, k -> k.getId().getValue(),
                            null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsDictionaryExcludeValueService service;

}

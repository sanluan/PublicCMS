package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

import freemarker.template.TemplateException;

/**
 *
 * dictionaryData 数据字典数据查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code>:数据字典id
 * <li><code>value</code>
 * 值,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsDictionaryData}
 * <li><code>values</code>
 * 多个值,逗号或空格间隔,当value为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.dictionaryData dictionaryId='data'
 * value='1'&gt;${object.text}&lt;/@cms.dictionaryData&gt;
 * <p>
 * &lt;@cms.dictionaryData dictionaryId values='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryData&gt;
 * 
 * <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/dictionaryData?dictionaryId=data&amp;value=1', function(data){    
     console.log(data.text);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String dictionaryId = handler.getString("dictionaryId");
        String value = handler.getString("value");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            if (CommonUtils.notEmpty(value)) {
                CmsDictionaryData entity = service.getEntity(new CmsDictionaryDataId(dictionaryId, siteId, value));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] values = handler.getStringArray("values");
                if (CommonUtils.notEmpty(values)) {
                    CmsDictionaryDataId[] ids = new CmsDictionaryDataId[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ids[i] = new CmsDictionaryDataId(dictionaryId, siteId, values[i]);
                    }
                    List<CmsDictionaryData> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryData> map = CommonUtils.listToMap(entityList, k -> k.getId().getValue(), null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsDictionaryDataService service;

}

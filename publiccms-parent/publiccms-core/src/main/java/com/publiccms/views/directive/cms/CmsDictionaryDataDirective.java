package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

import freemarker.template.TemplateException;

/**
 *
 * dictionaryData 数据字典数据查询指令
 * <p>
 * 上下文变量
 * <ul>
 * <li><code>lang</code>:语言
 * </ul>
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code>:数据字典id
 * <li><code>value</code>
 * 值,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsDictionaryData}
 * <li><code>values</code>
 * 多个值,逗号或空格间隔,当value为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;#assign lang="cn"/&gt;
 * &lt;@cms.dictionaryData dictionaryId='data'
 * value='1'&gt;${object.text}&lt;/@cms.dictionaryData&gt;
 * <p>
 * &lt;@cms.dictionaryData dictionaryId values='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryData&gt;
 *
 * <pre>
 *  &lt;script&gt;
   fetch('${site.dynamicPath}api/directive/cms/dictionaryData?dictionaryId=data&amp;value=1',{headers: {"lang":"cn"}}).then(res => res.json()).then(data=>{
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
        String lang = handler.getStringAttribute("lang");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            if (CommonUtils.notEmpty(value)) {
                CmsDictionaryData entity = service.getEntity(new CmsDictionaryDataId(dictionaryId, siteId, value));
                if (null != entity) {
                    entity.setAttribute(ExtendUtils.getExtendMap(entity.getLangdata()));
                    String text = entity.getAttribute().get(lang);
                    if (CommonUtils.notEmpty(lang) && CommonUtils.notEmpty(text)) {
                        entity.setText(text);
                    }
                    handler.put("object", entity).render();
                }
            } else {
                String[] values = handler.getStringArray("values");
                if (CommonUtils.notEmpty(values)) {
                    CmsDictionaryDataId[] ids = Stream.of(values).map(e -> new CmsDictionaryDataId(dictionaryId, siteId, e))
                            .toArray(CmsDictionaryDataId[]::new);
                    List<CmsDictionaryData> entityList = service.getEntitys(ids);
                    entityList.forEach(entity -> {
                        entity.setAttribute(ExtendUtils.getExtendMap(entity.getLangdata()));
                        String text = entity.getAttribute().get(lang);
                        if (CommonUtils.notEmpty(lang) && CommonUtils.notEmpty(text)) {
                            entity.setText(text);
                        }
                    });
                    Map<String, CmsDictionaryData> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().getValue(),
                            values, e -> e.getId().getValue());
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsDictionaryDataService service;

}

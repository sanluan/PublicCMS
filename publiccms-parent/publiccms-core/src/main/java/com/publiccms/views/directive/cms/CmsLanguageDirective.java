package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsLanguage;
import com.publiccms.entities.cms.CmsLanguageId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.cms.CmsLanguageService;

import freemarker.template.TemplateException;

/**
 *
 * language 数据字典数据查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>code</code>:语言编码,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsLanguage}
 * <li><code>codes</code>
 * 多个编码,逗号或空格间隔,当code为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@cms.Language code='cn' value='1'&gt;${object.name}&lt;/@cms.Language&gt;
 * <p>
 * &lt;@cms.Language code values='cn,en,jp'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.Language&gt;
 *
 * <pre>
 *  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/Language?code=cn', function(data){
     console.log(data.text);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsLanguageDirective extends AbstractTemplateDirective {

    @Resource
    protected FileUploadComponent fileUploadComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String code = handler.getString("code");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(code)) {
            CmsLanguage entity = service.getEntity(new CmsLanguageId(code, site.getId()));
            if (null != entity) {
                if (absoluteURL) {
                    entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
                }
                handler.put("object", entity).render();
            }
        } else {
            String[] codes = handler.getStringArray("codes");
            if (CommonUtils.notEmpty(codes)) {
                CmsLanguageId[] ids = Stream.of(codes).map(e -> new CmsLanguageId(e, site.getId())).toArray(CmsLanguageId[]::new);
                List<CmsLanguage> entityList = service.getEntitys(ids);
                UnaryOperator<CmsLanguage> valueMapper = entity -> {
                    entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
                    return entity;
                };
                Map<String, CmsLanguage> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().getCode(),
                        absoluteURL ? valueMapper : null, codes, e -> e.getId().getCode(), null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsLanguageService service;

}

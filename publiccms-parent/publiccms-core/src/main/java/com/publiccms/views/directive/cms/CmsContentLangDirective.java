package com.publiccms.views.directive.cms;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentLang;
import com.publiccms.entities.cms.CmsContentLangId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentLangService;
import com.publiccms.logic.service.cms.CmsContentService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * contentLang 分类语言查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:分类id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsContentLang}
 * <li><code>lang</code>:语言
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@cms.contentLang id=1 lang=cn&gt;${object.name}&lt;/@cms.content&gt;
 *
 * <pre>
   &lt;script&gt;
    fetch('${site.dynamicPath}api/directive/cms/contentLang?id=1&amp;lang=cn',{"headers":{"appToken":"接口访问授权Token"}}).then(res => res.json()).then(data=>{
      console.log(data.name);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsContentLangDirective extends AbstractTemplateDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        String lang = handler.getString("lang");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(lang)) {
            CmsContent content = contentService.getEntity(id);
            if (null != content && site.getId() == content.getSiteId()) {
                CmsContentLang entity = service.getEntity(new CmsContentLangId(id, lang));
                if (null != entity) {
                    entity.setAttribute(ExtendUtils.getExtendMap(entity.getData()));
                    handler.put("object", entity).render();
                }
            }
        }
    }

    /**
     * @return whether need the app token
     */
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsContentLangService service;
}

package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;

import freemarker.template.TemplateException;

/**
 *
 * content 内容查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>
 * 内容id,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsContent}
 * <li><code>absoluteURL</code>:url处理为绝对路径 默认为<code> true</code>
 * <li><code>absoluteId</code>:id处理为引用内容的ID 默认为<code> true</code>
 * <li><code>containsAttribute</code>
 * 默认为<code>false</code>,http请求时为高级选项,为true时<code>object.attribute</code>为内容扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>ids</code>:
 * 多个内容id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.content id=1&gt;${object.title}&lt;/@cms.content&gt;
 * <p>
 * &lt;@cms.content ids=1,2,3&gt;&lt;#list map as
 * k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.content&gt;
 * 
 * <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/content?id=1', function(data){    
     console.log(data.title);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsContentDirective extends AbstractTemplateDirective {
    @Resource
    protected ContentConfigComponent contentConfigComponent;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean absoluteId = handler.getBoolean("absoluteId", true);
        boolean containsAttribute = handler.getBoolean("containsAttribute", false) && (!handler.inHttp() || getAdvanced(handler));
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsContent entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                ClickStatistics statistics = statisticsComponent.getContentStatistics(entity.getId());
                if (null != statistics) {
                    entity.setClicks(entity.getClicks() + statistics.getClicks());
                }
                if (absoluteId && null == entity.getParentId() && null != entity.getQuoteContentId()) {
                    entity.setId(entity.getQuoteContentId());
                }
                if (absoluteURL) {
                    CmsUrlUtils.initContentUrl(site, entity);
                    fileUploadComponent.initContentCover(site, entity);
                }
                if (containsAttribute) {
                    entity.setAttribute(ExtendUtils.getAttributeMap(attributeService.getEntity(id),
                            contentConfigComponent.getKeywordsConfig(site.getId())));
                }
                handler.put("object", entity);
                handler.render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                KeywordsConfig config = containsAttribute ? contentConfigComponent.getKeywordsConfig(site.getId()) : null;
                Map<Long, CmsContentAttribute> attributeMap = containsAttribute
                        ? CommonUtils.listToMap(attributeService.getEntitys(ids), k -> k.getContentId())
                        : null;
                Consumer<CmsContent> consumer = e -> {
                    ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                    if (null != statistics) {
                        e.setClicks(e.getClicks() + statistics.getClicks());
                    }
                    if (absoluteId && null == e.getParentId() && null != e.getQuoteContentId()) {
                        e.setId(e.getQuoteContentId());
                    }
                    if (absoluteURL) {
                        CmsUrlUtils.initContentUrl(site, e);
                        fileUploadComponent.initContentCover(site, e);
                    }
                    if (containsAttribute) {
                        e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId()), config));
                    }
                };
                Map<String, CmsContent> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsContentService service;
}

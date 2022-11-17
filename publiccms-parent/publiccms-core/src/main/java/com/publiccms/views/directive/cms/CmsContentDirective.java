package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
*
* content 内容查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 内容id,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsContent} 
* <li><code>absoluteURL</code> url处理为绝对路径 默认为<code> true</code>
* <li><code>absoluteId</code> url处理为绝对路径 默认为<code> true</code>
* <li><code>containsAttribute</code> id不为空时有效,默认为<code>false</code>,结果返回<code>attribute</code>内容扩展数据<code>map</code>(字段编码,<code>value</code>)
* <li><code>ids</code> 多个内容id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
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

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean absoluteId = handler.getBoolean("absoluteId", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsContent entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                ClickStatistics statistics = statisticsComponent.getContentStatistics(entity.getId());
                if (null != statistics) {
                    entity.setClicks(entity.getClicks() + statistics.getClicks());
                }
                if (absoluteId && null != entity.getQuoteContentId()) {
                    entity.setId(entity.getQuoteContentId());
                }
                if (absoluteURL) {
                    TemplateComponent.initContentUrl(site, entity);
                    TemplateComponent.initContentCover(site, entity);
                }
                handler.put("object", entity);
                if (handler.getBoolean("containsAttribute", false)) {
                    CmsContentAttribute attribute = attributeService.getEntity(id);
                    if (null != attribute) {
                        Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                        map.put("text", attribute.getText());
                        map.put("source", attribute.getSource());
                        map.put("sourceUrl", attribute.getSourceUrl());
                        map.put("wordCount", String.valueOf(attribute.getWordCount()));
                        map.put("minPrice", String.valueOf(attribute.getMinPrice()));
                        map.put("maxPrice", String.valueOf(attribute.getMaxPrice()));
                        handler.put("attribute", map);
                    }
                }
                handler.render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                Consumer<CmsContent> consumer;
                if (absoluteURL) {
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                        if (absoluteId &&null==e.getParentId() &&  null != e.getQuoteContentId()) {
                            e.setId(e.getQuoteContentId());
                        }
                        TemplateComponent.initContentUrl(site, e);
                        TemplateComponent.initContentCover(site, e);
                    };
                } else {
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (absoluteId && null==e.getParentId() && null != e.getQuoteContentId()) {
                            e.setId(e.getQuoteContentId());
                        }
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                    };
                }
                Map<String, CmsContent> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsContentService service;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private StatisticsComponent statisticsComponent;
}

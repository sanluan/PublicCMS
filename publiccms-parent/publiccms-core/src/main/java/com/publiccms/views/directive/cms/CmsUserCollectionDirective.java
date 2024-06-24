package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserCollection;
import com.publiccms.entities.cms.CmsUserCollectionId;
import com.publiccms.logic.service.cms.CmsUserCollectionService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * userCollection 用户收藏查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code>:用户id
 * <li><code>contentId</code>:内容id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsUserCollection}
 * <li><code>contentIds</code>
 * 多个项目id,逗号或空格间隔,当contentId为空时生效,结果返回<code>map</code>(contentId,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.userCollection userId=1
 * contentId=1&gt;${object.scores}&lt;/@cms.userCollection&gt;
 * <p>
 * &lt;@cms.userCollection userId=1 contentIds='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.contentId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.userCollection&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/userCollection?id=1', function(data){    
  console.log(data.scores);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsUserCollectionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long userId = handler.getLong("userId");
        Long contentId = handler.getLong("contentId");
        if (null != userId) {
            if (null != contentId) {
                CmsUserCollection entity = service.getEntity(new CmsUserCollectionId(userId, contentId));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                Long[] contentIds = handler.getLongArray("contentIds");
                if (CommonUtils.notEmpty(contentIds)) {
                    CmsUserCollectionId[] entityIds = new CmsUserCollectionId[contentIds.length];
                    for (int i = 0; i < contentIds.length; i++) {
                        entityIds[i] = new CmsUserCollectionId(userId, contentIds[i]);
                    }
                    List<CmsUserCollection> entityList = service.getEntitys(entityIds);
                    Map<String, CmsUserCollection> map = CommonUtils.listToMap(entityList,
                            k -> String.valueOf(k.getId().getContentId()), null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsUserCollectionService service;

}

package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsVoteService;

/**
 *
 * vote 投票查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code> 投票id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsVote}
 * <li><code>ids</code>
 * 多个投票id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.vote id=1&gt;${object.title}&lt;/@cms.vote&gt;
 * <p>
 * &lt;@cms.vote ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.vote&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/vote?id=1', function(data){    
  console.log(data.title);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsVoteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsVote entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsVote> entityList = service.getEntitys(ids);
                Map<String, CmsVote> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsVoteService service;

}

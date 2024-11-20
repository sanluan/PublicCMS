package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.entities.cms.CmsUserVoteId;
import com.publiccms.logic.service.cms.CmsUserVoteService;

import freemarker.template.TemplateException;

/**
 *
 * userVote 用户投票查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code>:用户id
 * <li><code>voteId</code>:投票id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsUserVote}
 * <li><code>voteIds</code>
 * 多个投票id,逗号或空格间隔,当voteId为空时生效,结果返回<code>map</code>(voteId,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.userVote id=1&gt;${object.itemId}&lt;/@cms.userVote&gt;
 * <p>
 * &lt;@cms.userVote ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.itemId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.userVote&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/userVote?id=1', function(data){    
  console.log(data.itemId);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsUserVoteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long userId = handler.getLong("userId");
        Long voteId = handler.getLong("voteId");
        if (null != userId && null != voteId) {
            CmsUserVote entity = service.getEntity(new CmsUserVoteId(userId, voteId));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] voteIds = handler.getLongArray("voteIds");
            if (CommonUtils.notEmpty(voteIds)) {
                CmsUserVoteId[] entityIds = new CmsUserVoteId[voteIds.length];
                for (int i = 0; i < voteIds.length; i++) {
                    entityIds[i] = new CmsUserVoteId(userId, voteIds[i]);
                }
                List<CmsUserVote> entityList = service.getEntitys(entityIds);
                Map<String, CmsUserVote> map = CommonUtils.listToMapSorted(entityList, k -> String.valueOf(k.getId().getVoteId()), voteIds);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsUserVoteService service;

}

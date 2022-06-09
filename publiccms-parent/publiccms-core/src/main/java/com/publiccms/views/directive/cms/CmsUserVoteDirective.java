package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.entities.cms.CmsUserVoteId;
import com.publiccms.logic.service.cms.CmsUserVoteService;

/**
*
* tag 标签查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 标签id，结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsTag}
* <li><code>ids</code>
* 多个标签id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.tag id=1&gt;${object.name}&lt;/@cms.tag&gt;
* <p>
* &lt;@cms.tag ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.tag&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/cms/tag?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsUserVoteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
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
                Map<String, CmsUserVote> map = CommonUtils.listToMap(entityList, k -> String.valueOf(k.getId().getVoteId()), null,
                        null);
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsUserVoteService service;

}

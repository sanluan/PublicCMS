package com.publiccms.views.directive.cms;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCommentService;

/**
*
* comment 评论查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 分类id，结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsComment}
* <li><code>ids</code>
* 多个评论id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,评论)
* {@link com.publiccms.entities.cms.CmsComment}
* </ul>
* 使用示例
* <p>
* &lt;@_comment id=1&gt;${object.text}&lt;/@_comment&gt;
* <p>
* &lt;@_comment ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${v.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_comment&gt;
* 
* <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/comment?id=1&amp;appToken=接口访问授权Token', function(data){    
     console.log(data.text);
   });
   &lt;/script&gt;
* </pre>
*/
@Component
public class CmsCommentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsComment entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsComment> entityList = service.getEntitys(ids);
                Map<String, CmsComment> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsCommentService service;

}

package com.publiccms.views.directive.sys;

// Generated 2022-2-9 10:41:51 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysLock;
import com.publiccms.entities.sys.SysLockId;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.sys.SysLockService;

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
public class SysLockDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (CommonUtils.notEmpty(itemType)) {
            short siteId = getSite(handler).getId();
            if (CommonUtils.notEmpty(itemId)) {
                SysLock entity = service.getEntity(new SysLockId(siteId, itemType, itemId));
                if (null != entity) {
                    int expriy = lockComponent.getExpriy(siteId, itemType);
                    if (entity.getCreateDate().before(DateUtils.addMinutes(CommonUtils.getDate(), -expriy))) {
                        entity = null;
                    }
                }
                handler.put("object", entity).render();
            } else {
                String[] itemIds = handler.getStringArray("itemIds");
                Long userId = handler.getLong("userId");
                if (CommonUtils.notEmpty(itemIds)) {
                    SysLockId[] entityIds = new SysLockId[itemIds.length];
                    for (int i = 0; i < itemIds.length; i++) {
                        entityIds[i] = new SysLockId(siteId, itemType, itemIds[i]);
                    }
                    List<SysLock> entityList = service.getEntitys(entityIds);
                    int expriy = lockComponent.getExpriy(siteId, itemType);
                    Map<String, SysLock> map = CommonUtils.listToMap(entityList, k -> String.valueOf(k.getId().getItemId()), null,
                            expriy > 0 ? f -> {
                                return f.getCreateDate().after(DateUtils.addMinutes(CommonUtils.getDate(), -expriy))
                                        && (null == f.getUserId() || null == userId || !f.getUserId().equals(userId));
                            } : null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private SysLockService service;
    @Autowired
    private LockComponent lockComponent;

}

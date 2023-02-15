package com.publiccms.views.directive.sys;

// Generated 2022-2-9 10:41:51 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import javax.annotation.Resource;
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
 * lock 锁查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>itemType</code>:项目类型
 * <li><code>itemId</code>:项目id,结果返回<code>object</code>
 * {@link com.publiccms.entities.sys.SysLock}
 * <li><code>itemIds</code>
 * 多个项目id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.lock id=1&gt;${object.count}&lt;/@sys.lock&gt;
 * <p>
 * &lt;@sys.lock ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.count}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.lock&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/sys/lock?id=1', function(data){    
  console.log(data.count);
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

    @Resource
    private SysLockService service;
    @Resource
    private LockComponent lockComponent;

}

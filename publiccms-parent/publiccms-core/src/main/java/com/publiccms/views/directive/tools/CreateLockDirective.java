package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysLock;
import com.publiccms.logic.component.site.LockComponent;

/**
 *
 * createLock 创建锁指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>itemType</code> 锁定项目类型
 * <li><code>itemId</code> 锁定项目id
 * <li><code>userId</code> 锁定用户id
 * <li><code>counter</code> 使用计数器,默认值<code>false</code>
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code>{@link com.publiccms.entities.sys.SysLock}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.createLock itemType='content' itemId='1'
 * userId='1'&gt;${url}&lt;/@tools.createLock&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/tools/createLock?itemType=content&amp;itemId=1&amp;userId=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CreateLockDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        Long userId = handler.getLong("userId");
        Boolean counter = handler.getBoolean("counter", false);
        SysLock entity = lockComponent.lock(getSite(handler).getId(), itemType, itemId, userId, counter);
        handler.put("object", entity).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LockComponent lockComponent;

}

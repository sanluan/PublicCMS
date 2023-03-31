package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItem;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;

import freemarker.template.TemplateException;

/**
 *
 * sysDeptItem 数据授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>deptId</code>:部门id
 * <li><code>ItemId</code>
 * 分类id,deptId、ItemId都存在时,结果返回<code>true</code>或<code>false</code>,表示该部门是否拥有该分类下内容的数据权限
 * <li><code>ItemIds</code>
 * 多个分类id,当deptId存在,且ItemId为空时生效,结果返回<code>map</code>(分类id,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.deptItem deptId=1 ItemId=1&gt;${object}&lt;/@sys.deptItem&gt;
 * <p>
 * &lt;@sys.deptItem deptId=1 ItemIds=1,2,3&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.deptItem&gt;
 *
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/deptItem?deptId=1&amp;ItemId=1&amp;appToken=接口访问授权Token', function(data){
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysDeptItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer deptId = handler.getInteger("deptId");
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (CommonUtils.notEmpty(deptId) && CommonUtils.notEmpty(itemType)) {
            if (CommonUtils.notEmpty(itemId)) {
                SysDept entity = sysDeptService.getEntity(deptId);
                handler.put("object", null != entity
                        && (entity.isOwnsAllCategory() || null != service.getEntity(new SysDeptItemId(deptId, itemType, itemId))))
                        .render();
            } else {
                String[] itemIds = handler.getStringArray("itemIds");
                if (CommonUtils.notEmpty(itemIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity) {
                        if (entity.isOwnsAllCategory()) {
                            for (String id : itemIds) {
                                map.put(id, true);
                            }
                        } else {
                            SysDeptItemId[] ids = new SysDeptItemId[itemIds.length];
                            for (int i = 0; i < itemIds.length; i++) {
                                map.put(itemIds[i], false);
                                ids[i] = new SysDeptItemId(deptId, itemType, itemIds[i]);
                            }
                            for (SysDeptItem e : service.getEntitys(ids)) {
                                map.put(e.getId().getItemId(), true);
                            }
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysDeptItemService service;
    @Resource
    private SysDeptService sysDeptService;
}

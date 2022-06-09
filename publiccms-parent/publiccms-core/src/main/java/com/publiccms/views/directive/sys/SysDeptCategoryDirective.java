package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * sysDeptCategory 部门分类授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>deptId</code> 部门id
 * <li><code>categoryId</code>
 * 分类id,deptId、categoryId都存在时，结果返回<code>true</code>或<code>false</code>，表示该部门是否拥有该分类下内容的数据权限
 * <li><code>categoryIds</code>
 * 多个分类id，当deptId存在，且categoryId为空时生效，结果返回<code>map</code>(分类id,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.deptCategory deptId=1
 * categoryId=1&gt;${object}&lt;/@sys.deptCategory&gt;
 * <p>
 * &lt;@sys.deptCategory deptId=1 categoryIds=1,2,3&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.deptCategory&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/sys/deptCategory?deptId=1&amp;categoryId=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysDeptCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        Integer categoryId = handler.getInteger("categoryId");
        if (CommonUtils.notEmpty(deptId)) {
            if (CommonUtils.notEmpty(categoryId)) {
                SysDept entity = sysDeptService.getEntity(deptId);
                handler.put("object", null != entity
                        && (entity.isOwnsAllCategory() || null != service.getEntity(new SysDeptCategoryId(deptId, categoryId))))
                        .render();
            } else {
                Integer[] categoryIds = handler.getIntegerArray("categoryIds");
                if (CommonUtils.notEmpty(categoryIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity) {
                        if (entity.isOwnsAllCategory()) {
                            for (Integer id : categoryIds) {
                                map.put(String.valueOf(id), true);
                            }
                        } else {
                            SysDeptCategoryId[] ids = new SysDeptCategoryId[categoryIds.length];
                            for (int i = 0; i < categoryIds.length; i++) {
                                map.put(String.valueOf(categoryIds[i]), false);
                                ids[i] = new SysDeptCategoryId(deptId, categoryIds[i]);
                            }
                            for (SysDeptCategory e : service.getEntitys(ids)) {
                                map.put(String.valueOf(e.getId().getCategoryId()), true);
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

    @Autowired
    private SysDeptCategoryService service;
    @Autowired
    private SysDeptService sysDeptService;
}

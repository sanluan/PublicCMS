package com.publiccms.views.directive.sys;

// Generated 2016-3-2 13:39:54 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * sysExtendFieldList 扩展字段列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>extendId</code> 扩展id
 * <li><code>inputType</code> 多个输入框类型【text:输入框,number:数字,textarea:多行文本,file:文件,image:图片,video:视频,password:密码,editor:百度编辑器,ckeditor:CK编辑器,tinymce:TinyMCE编辑器,kindeditor:KIND编辑器,date:日期,datetime:时间,color:颜色,alphaColor:带透明度的颜色,template:模板路径,boolean:是否,user:用户,dept:部门,content:内容,category:分类,dictionary:数据字典,categoryType:分类类型,tagType:标签类型,vote:投票,survey:调查问卷,tag:标签】
 * <li><code>searchable</code> 可搜索,【true,false】
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysExtendField}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.extendFieldList deptId=1 pageSize=10&gt;&lt;#list list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.extendFieldList&gt;
 *
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/extendFieldList?extendId=1&amp;appToken=接口访问授权Token', function(data){
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysExtendFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer extendId = handler.getInteger("extendId");
        String[] inputType = handler.getStringArray("inputType");
        Boolean searchable = handler.getBoolean("searchable");
        handler.put("list", service.getList(extendId, inputType, searchable)).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysExtendFieldService service;

}
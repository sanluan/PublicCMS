package com.publiccms.views.directive.log;

// Generated 2016-5-24 20:56:00 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.service.log.LogUploadService;

/**
 *
 * logUploadList 登录日志列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>fileTypes</code> 文件类型,【image,video,audio,document,other】
 * <li><code>image</code> 是否图片,【true,false】
 * <li><code>userId</code> 用户ID
 * <li><code>channel</code> 渠道
 * <li><code>originalName</code> 原文件名
 * <li><code>filePath</code> 文件路径
 * <li><code>orderField</code> 排序字段,[createDate:创建日期,fileSize:文件大小],默认ID倒叙
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.log.LogUpload}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@log.uploadList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@log.uploadList&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/log/UploadList?pageSize=10&amp;appToken=接口访问授权Token', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class LogUploadListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String[] fileTypes = handler.getStringArray("fileTypes");
        if (CommonUtils.empty(fileTypes) && handler.getBoolean("image", false)) {
            fileTypes = new String[] { CmsFileUtils.FILE_TYPE_IMAGE };
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getString("channel"),
                fileTypes, handler.getString("originalName"), handler.getString("filePath"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private LogUploadService service;

}
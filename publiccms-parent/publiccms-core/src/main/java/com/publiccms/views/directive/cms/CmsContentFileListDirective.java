package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.cms.CmsContentFileService;

import freemarker.template.TemplateException;

/**
 *
 * categoryFileList 内容附件列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>fileTypes</code>:文件类型,【image:图片,video:视频,audio:音频,other:其他】
 * <li><code>image</code>:是否图片,当fileTypes为空时有效,【true,false】
 * <li><code>contentId</code>:内容id
 * <li><code>userId</code>:用户id
 * <li><code>absoluteURL</code>:url处理为绝对路径 默认为<code>true</code>
 * <li><code>orderField</code>:排序字段,【size:文件大小,clicks:点击数】,默认排序正序、id正序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContentFile}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentFileList contentId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.filePath}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentFileList&gt;
 * 
 * <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/contentFileList?contentId=1&amp;pageSize=10', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class CmsContentFileListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String[] fileTypes = handler.getStringArray("fileTypes");
        if (CommonUtils.empty(fileTypes) && handler.getBoolean("image", false)) {
            fileTypes = new String[] { CmsFileUtils.FILE_TYPE_IMAGE };
        }
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getLong("userId"), fileTypes,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsContentFile> list = (List<CmsContentFile>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            SysSite site = getSite(handler);
            if (absoluteURL) {
                list.forEach(e -> e.setFilePath(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getFilePath())));
            }
        }
        handler.put("page", page).render();
    }

    @Resource
    private CmsContentFileService service;
    @Resource
    protected FileUploadComponent fileUploadComponent;
}
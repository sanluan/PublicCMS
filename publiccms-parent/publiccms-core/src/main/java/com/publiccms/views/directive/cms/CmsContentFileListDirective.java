package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentFileService;

/**
*
* contentList 内容列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>fileTypes</code> 文件类型,【image:图片,video:视频,audio:音频,other:其他】
* <li><code>image</code> 是否图片,当fileTypes为空时有效,【true,false】
* <li><code>contentId</code> 内容id
* <li><code>userId</code> 用户id
* <li><code>absoluteURL</code> url处理为绝对路径 默认为<code>true</code>
* <li><code>orderField</code> 排序字段,【size,clicks】,默认排序正序、id正序
* <li><code>orderType</code> 排序类型,【asc,desc】，默认为倒叙
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果page子属性:
* <ul>
* <li><code>totalCount</code> int类型 数据总数
* <li><code>pageIndex</code> int类型 当前页码
* <li><code>list</code> List类型 查询结果实体列表 {@link com.publiccms.entities.cms.CmsContentFile} 
* <li><code>totalPage</code> int类型 总页数
* <li><code>firstResult</code> int类型 第一条序号
* <li><code>firstPage</code> boolean类型 是否第一页
* <li><code>lastPage</code> boolean类型 是否最后一页
* <li><code>nextPage</code> int类型 下一页页码
* <li><code>prePage</code> int类型 上一页页码
* </ul>
* 使用示例
* <p>
* &lt;@_contentFileList contentId=1 pageSize=10&gt;&lt;#list page.list as a&gt;${a.filePath}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_contentFileList&gt;
* 
* <pre>
*  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/contentFileList?pageSize=10', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
* </pre>
* 
*/
@Component
public class CmsContentFileListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
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
            list.forEach(e -> {
                if (absoluteURL) {
                    e.setFilePath(TemplateComponent.getUrl(site, true, e.getFilePath()));
                }
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentFileService service;
}
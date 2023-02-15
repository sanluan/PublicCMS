package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ImageUtils;
import com.publiccms.entities.sys.SysSite;

/**
 * thumb 缩略图指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code>:文件路径
 * <li><code>width</code>:宽度
 * <li><code>height</code>:高度
 * </ul>
 * <p>
 * 打印结果文件路径
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.thumb path='images/logo.jpg' width=100 height=100/&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/thumb?path=images/logo.jpg&amp;width=100&amp;height=100&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.deviceType);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class ThumbDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Integer width = handler.getInteger("width");
        Integer height = handler.getInteger("height");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(path) && null != width && null != height
                && (path.startsWith(site.getSitePath()) || (!path.contains("://") && !path.startsWith("/")))) {
            String filepath;
            if (path.startsWith(site.getSitePath())) {
                filepath = path.substring(site.getSitePath().length());
            } else {
                filepath = path;
            }
            String suffix = CmsFileUtils.getSuffix(filepath);
            String thumbPath = filepath.substring(0, filepath.lastIndexOf(CommonConstants.DOT)) + CommonConstants.UNDERLINE
                    + width + CommonConstants.UNDERLINE + height + suffix;
            String thumbFilePath = siteComponent.getWebFilePath(site.getId(), thumbPath);
            if (CmsFileUtils.exists(thumbFilePath)) {
                handler.print(site.getSitePath() + thumbPath);
            } else {
                String sourceFilePath = siteComponent.getWebFilePath(site.getId(), filepath);
                if (CmsFileUtils.exists(sourceFilePath)) {
                    try {
                        ImageUtils.thumb(sourceFilePath, thumbFilePath, width, height, suffix);
                        handler.print(site.getSitePath() + thumbPath);
                    } catch (IOException e) {
                        handler.print(path);
                        log.error(e.getMessage());
                    }
                } else {
                    handler.print(path);
                }
            }
        } else {
            handler.print(path);
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}

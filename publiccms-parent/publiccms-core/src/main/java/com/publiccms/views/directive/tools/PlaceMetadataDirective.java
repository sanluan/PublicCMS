package com.publiccms.views.directive.tools;

import java.io.IOException;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

/**
 * placeMetadata 页面片段元数据获取指令
 * <p lang="zh">参数列表
 * <p lang="en">parameter list
 * <p lang="ja">パラメータリスト
 * <ul>
 * <li><code>path</code>:模板路径
 * </ul>
 * <p lang="zh">返回结果
 * <p lang="en">return result
 * <p lang="ja">戻り値
 * <ul>
 * <li><code>object</code>:
 * 元数据{@link com.publiccms.views.pojo.entities.CmsPlaceMetadata}
 * </ul>
 * <p lang="zh">使用示例
 * <p lang="en">usage example
 * <p lang="ja">使用例
 * <p>
 * &lt;@tools.placeMetadata
 * path='00000000-0000-0000-0000-000000000000'&gt;${object.alias}&lt;/@tools.placeMetadata&gt;
 *
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/placeMetadata?path=00000000-0000-0000-0000-000000000000.html&amp;appToken=接口访问授权Token', function(data){
   console.log(data.alias);
 });
 &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class PlaceMetadataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path) && !path.endsWith(Constants.SEPARATOR)) {
            String filepath = siteComponent.getTemplateFilePath(getSite(handler).getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            CmsPageData data = metadataComponent.getTemplateData(filepath);
            handler.put("object", metadata.getAsMap(data)).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private MetadataComponent metadataComponent;
}

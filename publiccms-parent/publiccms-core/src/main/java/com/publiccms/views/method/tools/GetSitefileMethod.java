package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.Constants;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.entities.Sitefile;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getSitefile 获取站点文件描述
 * <p>
 * 参数列表
 * <ol>
 * <li>文件名
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>sitefile</code>:{@link com.publiccms.views.pojo.entities.Sitefile}
 * </ul>
 * 使用示例
 * <p>
 * ${(getSitefile('aaa-site.zip').name)!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getSitefile?appToken=接口访问授权Token&amp;parameters=aaa-site.zip', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetSitefileMethod extends BaseMethod {
    @Resource
    protected SiteComponent siteComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        if (!arguments.isEmpty()) {
            String filepath = getString(0, arguments);
            File dest = new File(siteComponent.getSiteFilePath(filepath));
            try (ZipFile zipFile = ZipFile.builder().setFile(dest).setCharset(StandardCharsets.UTF_8).get()) {
                ZipArchiveEntry entry = zipFile.getEntry("description.json");
                if (null != entry) {
                    try (InputStream inputStream = zipFile.getInputStream(entry);) {
                        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                        return Constants.objectMapper.readValue(content,
                                Constants.objectMapper.getTypeFactory().constructType(Sitefile.class));
                    }
                }
            } catch (IOException e) {
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}

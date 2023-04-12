package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
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
        if (arguments.size() >= 1) {
            String filepath = getString(0, arguments);
            File dest = new File(siteComponent.getSiteFilePath(filepath));
            try (ZipFile zipFile = new ZipFile(dest, CommonConstants.DEFAULT_CHARSET_NAME);) {
                ZipEntry entry = zipFile.getEntry("description.json");
                if (null != entry) {
                    try (InputStream inputStream = zipFile.getInputStream(entry);) {
                        String content = IOUtils.toString(inputStream, CommonConstants.DEFAULT_CHARSET);
                        Sitefile sitefile = CommonConstants.objectMapper.readValue(content,
                                CommonConstants.objectMapper.getTypeFactory().constructType(Sitefile.class));
                        return sitefile;
                    }
                }
            } catch (IOException e) {
            }
        }
        return Collections.EMPTY_MAP;
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

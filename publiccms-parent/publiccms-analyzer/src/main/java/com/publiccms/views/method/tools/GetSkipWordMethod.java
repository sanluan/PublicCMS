package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getSkipWord 获取词典忽略分词
 * <p>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code> 词典忽略分词
 * </ul>
 * 使用示例
 * <p>
 * ${getSkipWord()}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getRandom?appToken=接口访问授权Token', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetSkipWordMethod extends BaseMethod {
    @Resource
    private SiteComponent siteComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        File dictFile = new File(
                CommonUtils.joinString(siteComponent.getRootPath(), AnalyzerDictUtils.DIR_DICT, AnalyzerDictUtils.TXT_SKIPWORD));
        try {
            return FileUtils.readFileToString(dictFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}

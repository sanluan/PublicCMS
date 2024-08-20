package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FontUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.TextConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.entities.ConfuseResult;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * confuse 混淆文字
 * <p>
 * 参数列表
 * <ol>
 * <li>字符串
 * <li>数字,默认为字符串长度的1/5
 * <li>布尔,是否html,默认否
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>ConfuseResult</code>:sha1值
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign result=confuse('&lt;a
 * href="http://www.publiccms.com"&gt;publiccms&lt;/a&gt;',11,true)/&gt;
 * &lt;style&gt;
 * 
 * @font-face {font-family:'confuse';src:url(${result.font});}
 *            #content{font-family:'confuse';} &lt;/style&gt; &lt;div
 *            id="content"&gt;${(result.text?no_esc)!} &lt;/div&gt;
 *            <p>
 * 
 *            <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/confuse?parameters=aaa', function(data){
console.log(data);
});
&lt;/script&gt;
 *            </pre>
 */
@Component
public class ConfuseMethod extends BaseMethod {
    @Resource
    protected ConfigDataComponent configDataComponent;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public Object execute(HttpServletRequest request, List<TemplateModel> arguments) throws TemplateModelException {
        SysSite site = (SysSite) request.getAttribute("site");
        return execute(site, arguments);
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        TemplateModel model = Environment.getCurrentEnvironment().getGlobalVariable(CommonConstants.getAttributeSite());
        SysSite site = null;
        if (null != model) {
            site = (SysSite) TemplateModelUtils.converBean(model);
        }
        return execute(site, arguments);
    }

    public Object execute(SysSite site, List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        Integer size = getInteger(1, arguments);
        Boolean html = getBoolean(2, arguments);
        ConfuseResult result = new ConfuseResult();
        result.setText(string);
        if (CommonUtils.notEmpty(string) && string.length() > 1) {
            Map<String, String> config = configDataComponent.getConfigData(site.getId(), TextConfigComponent.CONFIG_CODE);
            String fontfile = config.get(TextConfigComponent.CONFIG_CONFUSE_FONT);
            if (CommonUtils.notEmpty(fontfile)) {
                String fontfilePath = siteComponent.getPrivateFilePath(site.getId(), fontfile);
                String fonturl = CmsFileUtils.getFontFileName("ttf");
                String outFontfilePath = siteComponent.getWebFilePath(site.getId(), fonturl);
                try {
                    if (null == size) {
                        size = string.length() / 5;
                    }
                    if (null == html) {
                        html = false;
                    }
                    if (html) {
                        string = HtmlUtils.removeHtmlTag(string);
                    }
                    Map<Character, Character> swapWordMap = FontUtils.swapWordMap(new File(fontfilePath),
                            new File(outFontfilePath), string, size);
                    result.setText(HtmlUtils.swapWord(result.getText(), swapWordMap, html));
                    result.setFont(CommonUtils.joinString(site.getSitePath(), fonturl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
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

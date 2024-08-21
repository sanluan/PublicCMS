package com.publiccms.views.method.tools;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.google.typography.font.sfntly.Font;
import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FontUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.TextConfigComponent;
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
 * <li>数字,默认为字符数的1/5
 * <li>布尔,是否html,默认否
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>ConfuseResult</code>:sha1值
 * </ul>
 * 使用示例
 * 
 * <pre>
 * &lt;#assign result=confuse('&lt;a href="http://www.publiccms.com"&gt;publiccms&lt;/a&gt;',11,true)/&gt;
 * 
 * &lt;style&gt;
 *     &#64;font-face {font-family:confuse;src:url(${result.font});}
 *     #content{font-family:confuse !important;}
 * &lt;/style&gt;
 * &lt;div id="content"&gt;${(result.text?no_esc)!}&lt;/div&gt
 * &lt;script&gt;
 * var elements = document.querySelectorAll('#content > *');
 *   elements.forEach(function(element) {
 *       if(element.style.fontFamily){
 *           element.style.fontFamily='confuse,'+element.style.fontFamily;
 *       }
 *   });
 * &lt;/script&gt;
 * </pre>
 * 
 * <pre>
 * &lt;script&gt;
 * $.getJSON('${site.dynamicPath}api/method/confuse?parameters=aaa&amp;appToken=接口访问授权Token', function(data){
 *     console.log(data);
 * });
 * &lt;/script&gt;
 * </pre>
 */
@Component
public class ConfuseMethod extends BaseMethod {
    @Resource
    protected TextConfigComponent textConfigComponent;

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
        if (CommonUtils.notEmpty(string)) {
            Font font = textConfigComponent.getFont(site.getId());
            if (null != font) {
                try {
                    if (null == html) {
                        html = false;
                    } else if (html) {
                        string = HtmlUtils.removeHtmlTag(string);
                    }

                    List<Character> wordList = FontUtils.sortedCharList(string);
                    if (null == size) {
                        size = wordList.size() / 5;
                    }
                    Map<Character, Character> swapWordMap = FontUtils.swapWordMap(wordList, size);
                    result.setFont(FontUtils.generateFontData(font, swapWordMap));
                    result.setText(HtmlUtils.swapWord(result.getText(), swapWordMap, html));
                } catch (IOException e) {
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

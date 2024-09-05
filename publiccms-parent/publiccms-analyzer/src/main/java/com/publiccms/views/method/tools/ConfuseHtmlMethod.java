package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysSite;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * confuse 混淆html中的文字
 * <p>
 * 参数列表
 * <ol>
 * <li>字符串
 * <li>数字,默认为字符数的1/5
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>ConfuseResult</code>:map{text:加密文本,font:加密字体}
 * </ul>
 * 使用示例
 * 
 * <pre>
 * &lt;#assign result=confuseHtml('&lt;a href="http://www.publiccms.com"&gt;publiccms&lt;/a&gt;',11)/&gt;
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
 * $.getJSON('${site.dynamicPath}api/method/confuseHtml?parameters=aaa&amp;appToken=接口访问授权Token', function(data){
 *     console.log(data);
 * });
 * &lt;/script&gt;
 * </pre>
 */
@Component
public class ConfuseHtmlMethod extends ConfuseMethod {

    public Object execute(SysSite site, List<TemplateModel> arguments) throws TemplateModelException {
        return confuse(site, getString(0, arguments), getInteger(1, arguments), true);
    }
}

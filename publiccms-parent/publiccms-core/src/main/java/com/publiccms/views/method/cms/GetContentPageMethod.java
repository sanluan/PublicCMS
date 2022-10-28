package com.publiccms.views.method.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getContentPage 获取正文分页结果
* <p>
* 参数列表
* <ol>
* <li>正文
* <li>页码
* </ol>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>text</code>文章正文
* </ul>
* 使用示例
* <p>
* &lt;#assign textPage=getContentPage(attribute.text,2)/&lt;
* <p>
* ${textPage.text?no_esc!}
* current page :${textPage.page.pageIndex}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getContentAttributes?parameters=正文内容&amp;parameters=2', function(data){
console.log(data.text);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetContentPageMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String text = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        Map<String, Object> resultMap = new HashMap<>();
        if (CommonUtils.notEmpty(text)) {
            String pageBreakTag = null;
            if (text.contains(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (text.contains(CommonConstants.getTinyMCEPageBreakTag())) {
                pageBreakTag = CommonConstants.getTinyMCEPageBreakTag();
            } else if (text.contains(CommonConstants.getKindEditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getKindEditorPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(text, pageBreakTag);
            PageHandler page = new PageHandler(pageIndex, 1);
            page.setTotalCount(texts.length);
            resultMap.put("page", page);
            resultMap.put("text", texts[page.getPageIndex() - 1]);
        }
        return resultMap;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}

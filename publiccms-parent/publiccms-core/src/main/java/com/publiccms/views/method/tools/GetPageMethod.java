package com.publiccms.views.method.tools;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getPage 获取分页url
 * <p>
 * 参数列表
 * <ol>
 * <li><code>url</code>
 * <li><code>pageIndex</code>页码数字
 * <li><code>string</code>分页参数,动态url分页参数名,为空时返回静态化url分页结果
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code> 带分页的url
 * </ul>
 * 使用示例
 * <p>
 * ${getPage('https://www.publiccms.com/introduction/index_3.html',2)}
 * <p>
 * ${getPage('https://search.publiccms.com/?word=cms',2,'pageIndex')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getHash?parameters=https://www.publiccms.com/introduction/index_3.html&amp;parameters=2', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetPageMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        String pageParameter = getString(2, arguments);
        if (CommonUtils.notEmpty(url)) {
            if (CommonUtils.notEmpty(pageIndex)) {
                return getPageUrl(url, pageIndex, pageParameter);
            } else {
                return getPageUrl(url, 1, pageParameter);
            }
        }
        return url;
    }

    public String getPageUrl(String url, int pageIndex, String pageParameter) {
        if (CommonUtils.notEmpty(pageParameter)) {
            int splitIndex = url.lastIndexOf('?');
            if (-1 < splitIndex) {
                boolean flag = true;
                String[] parameters = StringUtils.split(url.substring(splitIndex + 1), '&');
                for (int i = 0; i < parameters.length; i++) {
                    String[] temp = StringUtils.split(parameters[i], "=", 2);
                    if (pageParameter.equalsIgnoreCase(temp[0])) {
                        parameters[i] = pageParameter + "=" + pageIndex;
                        flag = false;
                        break;
                    }
                }
                StringBuilder sb = new StringBuilder(url.substring(0, splitIndex)).append("?")
                        .append(arrayToDelimitedString(parameters, "&"));
                if (flag && 1 != pageIndex) {
                    sb.append("&").append(pageParameter).append("=").append(pageIndex);
                }
                return sb.toString();
            } else {
                if (1 != pageIndex) {
                    return new StringBuilder(url).append("?").append(pageParameter).append("=").append(pageIndex).toString();
                } else {
                    return url;
                }
            }
        } else {
            if (url.endsWith(CommonConstants.SEPARATOR) && 1 != pageIndex) {
                url += CommonConstants.getDefaultPage();
            }
            int dotIndex = url.lastIndexOf(CommonConstants.DOT);
            int separatorIndex = url.lastIndexOf(CommonConstants.SEPARATOR);
            int underlineIndex = url.lastIndexOf(CommonConstants.UNDERLINE);
            if (separatorIndex < dotIndex) {
                String prefixFilePath = url.substring(0, dotIndex);
                String suffixFilePath = url.substring(dotIndex, url.length());
                if (separatorIndex < underlineIndex) {
                    try {
                        Integer.parseInt(prefixFilePath.substring(underlineIndex + 1, prefixFilePath.length()));
                        prefixFilePath = prefixFilePath.substring(0, underlineIndex);
                    } catch (NumberFormatException e) {
                    }
                }
                if (1 < pageIndex) {
                    return prefixFilePath + CommonConstants.UNDERLINE + pageIndex + suffixFilePath;
                } else {
                    return prefixFilePath + suffixFilePath;
                }
            } else {
                String prefixFilePath = url;
                if (separatorIndex < underlineIndex) {
                    prefixFilePath = prefixFilePath.substring(0, underlineIndex);
                }
                if (1 < pageIndex) {
                    return prefixFilePath + CommonConstants.UNDERLINE + pageIndex;
                } else {
                    return prefixFilePath;
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 2;
    }
}

package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

/**
 * exception 异常获取
 * 返回结果
 * <ul>
 * <li><code>object</code>异常字符串
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.exception&gt;${object}&lt;/@tools.exception&gt;
 * 
 * 
 */
@Component
public class ExceptionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Throwable throwable;
        try {
            throwable = (Throwable) handler.getRequest().getAttribute("javax.servlet.error.exception");
        } catch (Exception e) {
            throwable = e;
        }
        handler.put("object", exceptionMsgForInner(throwable));
        handler.render();
    }

    private static String exceptionMsgForInner(java.lang.Throwable e) {
        if (null != e) {
            String errorMessage = e.getLocalizedMessage();
            if (null != errorMessage) {
                StringBuilder sb = new StringBuilder();
                sb.append(errorMessage).append("\r\n");
                if (null != e.getCause()) {
                    String result = exceptionMsgForInner(e.getCause());
                    if (null != result) {
                        sb.append(result);
                    }
                } else {
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        sb.append(stackTraceElement.toString()).append("\r\n");
                    }
                }
                return sb.toString();
            }
        }
        return null;
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }
}

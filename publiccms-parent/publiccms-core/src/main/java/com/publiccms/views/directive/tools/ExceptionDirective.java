package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

/**
 * ExceptionDirective
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
        String errorMessage = e.getLocalizedMessage();
        if (null == errorMessage) {
            errorMessage = "";
        }
        errorMessage += "\r\n";
        if (null != e.getCause()) {
            errorMessage += exceptionMsgForInner(e.getCause());
        } else {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                errorMessage += stackTraceElement.toString() + "\r\n";
            }
        }
        return errorMessage.trim();
    }
}

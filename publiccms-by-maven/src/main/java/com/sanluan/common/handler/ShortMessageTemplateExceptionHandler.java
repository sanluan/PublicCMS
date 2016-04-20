package com.sanluan.common.handler;

import java.io.IOException;
import java.io.Writer;

import com.sanluan.common.base.Base;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ShortMessageTemplateExceptionHandler extends Base implements TemplateExceptionHandler {
    /*
     * (non-Javadoc)
     * 
     * @see
     * freemarker.template.TemplateExceptionHandler#handleTemplateException(
     * freemarker.template.TemplateException, freemarker.core.Environment,
     * java.io.Writer)
     */
    @Override
    public void handleTemplateException(TemplateException templateexception, Environment environment, Writer writer)
            throws TemplateException {
        try {
            String code = templateexception.getMessage();
            if (code.indexOf("Failed at:") > 0 && code.indexOf("[in") > 0) {
                code = code.substring(code.indexOf("Failed at:") + 10, code.length());
                code = code.substring(0, code.indexOf("[in"));
                writer.write(code);
            } else {
                writer.write("[SOME ERRORS OCCURRED！]");
            }
        } catch (IOException e) {
            log.error(environment.getCurrentTemplate().getSourceName() + ":" + e.getMessage());
        }
    }

}

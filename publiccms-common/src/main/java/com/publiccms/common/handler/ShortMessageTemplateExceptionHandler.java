package com.publiccms.common.handler;

import java.io.IOException;
import java.io.Writer;

import com.publiccms.common.base.Base;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 *
 * ShortMessageTemplateExceptionHandler
 * 
 */
public class ShortMessageTemplateExceptionHandler extends Base implements TemplateExceptionHandler {
    
    @Override
    public void handleTemplateException(TemplateException templateexception, Environment environment, Writer writer)
            throws TemplateException {
        try {
            String code = templateexception.getFTLInstructionStack();
            if (null != code && code.indexOf("Failed at:") > 0 && code.indexOf("[in") > 0) {
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

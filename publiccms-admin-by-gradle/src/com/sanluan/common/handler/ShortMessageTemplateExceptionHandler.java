package com.sanluan.common.handler;

import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ShortMessageTemplateExceptionHandler implements TemplateExceptionHandler {
<<<<<<< HEAD
	private final Log log = getLog(getClass());
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
			log.debug(e.getMessage());
		}
	}
=======
    private final Log log = getLog(getClass());
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
            log.debug(e.getMessage());
        }
    }
>>>>>>> b7117fb2de906a985a5be5015f24f8c6b6b5a315

}

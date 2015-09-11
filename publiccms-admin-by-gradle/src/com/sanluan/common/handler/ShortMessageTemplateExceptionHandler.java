package com.sanluan.common.handler;

import java.io.IOException;
import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ShortMessageTemplateExceptionHandler implements TemplateExceptionHandler {

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
			writer.write("[SOME ERRORS OCCURRED！]");
		} catch (IOException e) {
		}
	}

}

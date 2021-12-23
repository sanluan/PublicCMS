package com.publiccms.common.tools;

import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.xml.sax.ContentHandler;

import fr.opensagres.poi.xwpf.converter.core.IXWPFConverter;
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLMapper;

public class CustomXHTMLConverter extends XHTMLConverter {
    private static final IXWPFConverter<XHTMLOptions> INSTANCE = new CustomXHTMLConverter();

    public static IXWPFConverter<XHTMLOptions> getInstance() {
        return INSTANCE;
    }

    @Override
    public void convert(XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options)
            throws XWPFConverterException, IOException {
        try {
            options = options != null ? options : XHTMLOptions.getDefault();
            XHTMLMapper mapper = new CustomXHTMLMapper(document, contentHandler, options);
            mapper.start();
        } catch (Exception e) {
            throw new XWPFConverterException(e);
        }
    }
}

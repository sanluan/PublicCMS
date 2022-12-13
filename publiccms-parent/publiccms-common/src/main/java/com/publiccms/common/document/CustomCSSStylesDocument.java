package com.publiccms.common.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSProperty;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStyle;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylesDocument;

public class CustomCSSStylesDocument extends CSSStylesDocument {
    private Map<String, CSSStyle> map;

    public CustomCSSStylesDocument(XWPFDocument document, Integer indent) throws XmlException, IOException {
        super(document, false, indent);
    }

    @Override
    protected void initialize() throws XmlException, IOException {
        map = new HashMap<>();
        super.initialize();
    }

    @Override
    public CSSStyle createCSSStyle(CTPPrBase pPr, String className) {
        CSSStyle style = super.createCSSStyle(pPr, className);
        putStyle(className, style);
        return style;
    }

    @Override
    public CSSStyle createCSSStyle(CTRPr rPr, String className) {
        CSSStyle style = super.createCSSStyle(rPr, className);
        putStyle(className, style);
        return style;
    }

    @Override
    public CSSStyle createCSSStyle(CTTblPrBase tblPr, String className) {
        CSSStyle style = super.createCSSStyle(tblPr, className);
        putStyle(className, style);
        return style;
    }

    @Override
    public CSSStyle createCSSStyle(CTTrPr trPr, String className) {
        CSSStyle style = super.createCSSStyle(trPr, className);
        putStyle(className, style);
        return style;
    }

    @Override
    public CSSStyle createCSSStyle(CTTcPr tcPr, String className) {
        CSSStyle style = super.createCSSStyle(tcPr, className);
        putStyle(className, style);
        return style;
    }

    public void putStyle(String className, CSSStyle style) {
        CSSStyle oldStyle = map.get(className);
        if (null == oldStyle) {
            map.put(className, style);
        } else if (null != style) {
            for (CSSProperty p : style.getProperties()) {
                oldStyle.addProperty(p.getName(), p.getValue());
            }
        }
    }

    public CSSStyle getCSSStyle(String className) {
        return map.get(className);
    }
}

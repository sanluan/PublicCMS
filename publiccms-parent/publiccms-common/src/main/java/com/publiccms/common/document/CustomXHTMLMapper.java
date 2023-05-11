package com.publiccms.common.document;

import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.A_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.COLSPAN_ATTR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.HREF_ATTR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.P_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.ROWSPAN_ATTR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.SPAN_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.STYLE_ATTR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TABLE_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TD_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TH_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TR_ELEMENT;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STVerticalAlignRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.publiccms.common.tools.CommonUtils;

import fr.opensagres.poi.xwpf.converter.core.BorderSide;
import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.ListItemContext;
import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.styles.run.RunFontStyleStrikeValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.run.RunTextHighlightingValueProvider;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLMapper;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStyle;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.StringEscapeUtils;

public class CustomXHTMLMapper extends XHTMLMapper {
    static final String TAB_CHAR_SEQUENCE = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private final ContentHandler contentHandler;
    private XWPFParagraph currentParagraph;
    private AttributesImpl currentRunAttributes;

    public CustomXHTMLMapper(XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options) throws Exception {
        super(document, contentHandler, options);
        this.contentHandler = contentHandler;
    }

    @Override
    protected XWPFStylesDocument createStylesDocument(XWPFDocument document) throws XmlException, IOException {
        return new CustomCSSStylesDocument(document, options.getIndent());
    }

    @Override
    protected Object startVisitParagraph(XWPFParagraph paragraph, ListItemContext itemContext, Object parentContainer)
            throws Exception {
        List<CSSStyle> styleList = createStyleList(paragraph.getStyleID(), true);
        CTPPr pPr = paragraph.getCTP().getPPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle(pPr);
        AttributesImpl attributes = createStyleAttribute(cssStyle, styleList);

        startElement(P_ELEMENT, attributes);

        if (itemContext != null) {
            startElement(SPAN_ELEMENT, attributes);
            String text = itemContext.getText();
            if (StringUtils.isNotEmpty(text)) {
                text = CommonUtils.joinString(fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.replaceNonUnicodeChars(text),
                        "\u0020");
                SAXHelper.characters(contentHandler, StringEscapeUtils.escapeHtml(text));
            }
            endElement(SPAN_ELEMENT);
        }
        return null;
    }

    @Override
    protected void visitRun(XWPFRun run, boolean pageNumber, String url, Object paragraphContainer) throws Exception {
        if (run.getParent() instanceof XWPFParagraph) {
            this.currentParagraph = (XWPFParagraph) run.getParent();
            List<CSSStyle> styleList = createStyleList(currentParagraph.getStyleID());
            CTRPr rPr = run.getCTR().getRPr();
            CSSStyle cssStyle = getStylesDocument().createCSSStyle(rPr);
            this.currentRunAttributes = createStyleAttribute(cssStyle, styleList);
        }
        if (url != null) {
            AttributesImpl hyperlinkAttributes = new AttributesImpl();
            SAXHelper.addAttrValue(hyperlinkAttributes, HREF_ATTR, url);
            startElement(A_ELEMENT, hyperlinkAttributes);
        }
        super.visitRun(run, pageNumber, url, paragraphContainer);
        if (url != null) {
            characters(" ");
            endElement(A_ELEMENT);
        }
        this.currentRunAttributes = null;
        this.currentParagraph = null;
    }

    @Override
    protected void visitText(CTText ctText, boolean pageNumber, Object paragraphContainer) throws Exception {
        if (currentRunAttributes != null) {
            startElement(SPAN_ELEMENT, currentRunAttributes);
        }
        String text = ctText.getStringValue();
        if (StringUtils.isNotEmpty(text)) {
            characters(StringEscapeUtils.escapeHtml(text));
        }
        if (currentRunAttributes != null) {
            endElement(SPAN_ELEMENT);
        }
    }

    @Override
    protected void visitStyleText(XWPFRun run, String text, Object parent, boolean pageNumber) throws Exception {
        if (null == run.getFontFamily()) {
            run.setFontFamily(getStylesDocument().getFontFamilyAscii(run));
        }

        if (null == run.getFontSizeAsDouble() && null != getStylesDocument().getFontSize(run)) {
            run.setFontSize(getStylesDocument().getFontSize(run).doubleValue());
        }

        CTRPr rPr = run.getCTR().getRPr();

        List<CSSStyle> styleList = createStyleList(currentParagraph.getStyleID());

        CSSStyle cssStyle = getStylesDocument().createCSSStyle(rPr);
        if (cssStyle != null) {
            Color color = RunTextHighlightingValueProvider.INSTANCE.getValue(rPr, getStylesDocument());
            if (color != null)
                cssStyle.addProperty(CSSStylePropertyConstants.BACKGROUND_COLOR,
                        fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.toHexString(color));
            if (Boolean.TRUE.equals(RunFontStyleStrikeValueProvider.INSTANCE.getValue(rPr, getStylesDocument()))
                    || rPr.sizeOfDstrikeArray() > 0)
                cssStyle.addProperty("text-decoration", "line-through");
            if (rPr.sizeOfVertAlignArray() > 0) {
                int align = rPr.getVertAlignArray(0).getVal().intValue();
                if (STVerticalAlignRun.INT_SUPERSCRIPT == align) {
                    cssStyle.addProperty("vertical-align", "super");
                } else if (STVerticalAlignRun.INT_SUBSCRIPT == align) {
                    cssStyle.addProperty("vertical-align", "sub");
                }
            }
        }
        AttributesImpl runAttributes = createStyleAttribute(cssStyle, styleList);
        if (runAttributes != null) {
            startElement(SPAN_ELEMENT, runAttributes);
        }
        if (StringUtils.isNotEmpty(text)) {
            characters(StringEscapeUtils.escapeHtml(text));
        }
        if (runAttributes != null) {
            endElement(SPAN_ELEMENT);
        }
    }

    @Override
    protected void visitTabs(CTTabs tabs, Object paragraphContainer) throws Exception {
        if (currentParagraph != null && tabs == null) {
            startElement(SPAN_ELEMENT, null);
            characters(TAB_CHAR_SEQUENCE);
            endElement(SPAN_ELEMENT);
        }
    }

    @Override
    protected Object startVisitTable(XWPFTable table, float[] colWidths, Object tableContainer) throws Exception {
        List<CSSStyle> styleList = createStyleList(table.getStyleID());

        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle(tblPr);
        if (cssStyle != null) {
            cssStyle.addProperty(CSSStylePropertyConstants.BORDER_COLLAPSE, CSSStylePropertyConstants.BORDER_COLLAPSE_COLLAPSE);
        }
        AttributesImpl attributes = createStyleAttribute(cssStyle, styleList);

        startElement(TABLE_ELEMENT, attributes);
        return null;
    }

    @Override
    protected void startVisitTableRow(XWPFTableRow row, Object tableContainer, int rowIndex, boolean headerRow) throws Exception {
        XWPFTable table = row.getTable();
        List<CSSStyle> styleList = createStyleList(table.getStyleID());
        AttributesImpl attributes = createStyleAttribute(null, styleList);
        if (headerRow) {
            startElement(TH_ELEMENT, attributes);
        } else {
            startElement(TR_ELEMENT, attributes);
        }
    }

    @Override
    protected Object startVisitTableCell(XWPFTableCell cell, Object tableContainer, boolean firstRow, boolean lastRow,
            boolean firstCell, boolean lastCell, List<XWPFTableCell> vMergeCells) throws Exception {
        XWPFTableRow row = cell.getTableRow();
        XWPFTable table = row.getTable();
        List<CSSStyle> styleList = createStyleList(table.getStyleID());

        CTTcPr tcPr = cell.getCTTc().getTcPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle(tcPr);
        if (cssStyle != null) {
            TableCellBorder border = getStylesDocument().getTableBorder(table, BorderSide.TOP);
            if (border != null) {
                String style = CommonUtils.joinString(border.getBorderSize(), "px solid ",
                        fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.toHexString(border.getBorderColor()));
                cssStyle.addProperty(CSSStylePropertyConstants.BORDER_TOP, style);
            }

            border = getStylesDocument().getTableBorder(table, BorderSide.BOTTOM);
            if (border != null) {
                String style = CommonUtils.joinString(border.getBorderSize() , "px solid "
                        , fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.toHexString(border.getBorderColor()));
                cssStyle.addProperty(CSSStylePropertyConstants.BORDER_BOTTOM, style);
            }

            border = getStylesDocument().getTableBorder(table, BorderSide.LEFT);
            if (border != null) {
                String style =CommonUtils.joinString( border.getBorderSize() , "px solid "
                        , fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.toHexString(border.getBorderColor()));
                cssStyle.addProperty(CSSStylePropertyConstants.BORDER_LEFT, style);
            }

            border = getStylesDocument().getTableBorder(table, BorderSide.RIGHT);
            if (border != null) {
                String style = CommonUtils.joinString(border.getBorderSize() , "px solid "
                        , fr.opensagres.poi.xwpf.converter.core.utils.StringUtils.toHexString(border.getBorderColor()));
                cssStyle.addProperty(CSSStylePropertyConstants.BORDER_RIGHT, style);
            }
        }
        AttributesImpl attributes = createStyleAttribute(cssStyle, styleList);

        // colspan attribute
        BigInteger gridSpan = stylesDocument.getTableCellGridSpan(cell);
        if (gridSpan != null) {
            attributes = SAXHelper.addAttrValue(attributes, COLSPAN_ATTR, gridSpan.intValue());
        }

        if (vMergeCells != null) {
            attributes = SAXHelper.addAttrValue(attributes, ROWSPAN_ATTR, vMergeCells.size());
        }

        // 2) create element
        startElement(TD_ELEMENT, attributes);

        return null;
    }

    private void characters(String content) throws SAXException {
        SAXHelper.characters(contentHandler, content);
    }

    private void startElement(String name, Attributes attributes) throws SAXException {
        SAXHelper.startElement(contentHandler, name, attributes);
    }

    private void endElement(String name) throws SAXException {
        SAXHelper.endElement(contentHandler, name);
    }

    private List<CSSStyle> createStyleList(String styleId) {
        return createStyleList(styleId, false);
    }

    private List<CSSStyle> createStyleList(String styleId, boolean withDeafult) {
        if (StringUtils.isNotEmpty(styleId)) {
            CustomCSSStylesDocument styleDocument = (CustomCSSStylesDocument) getStylesDocument();
            String[] classNames = StringUtils.split(styleDocument.getClassNames(styleId));
            List<CSSStyle> styleList = new ArrayList<>();
            for (String className : classNames) {
                CSSStyle style = styleDocument.getCSSStyle(className);
                if (null != style) {
                    styleList.add(style);
                }
            }
            return styleList;
        }
        if (withDeafult) {
            CustomCSSStylesDocument styleDocument = (CustomCSSStylesDocument) getStylesDocument();
            List<CSSStyle> styleList = new ArrayList<>();
            CSSStyle style = styleDocument.getCSSStyle("a");
            if (null != style) {
                styleList.add(style);
            }
            return styleList;
        }
        return null;
    }

    private AttributesImpl createStyleAttribute(CSSStyle cssStyle, List<CSSStyle> styleList) {
        StringBuilder sb = new StringBuilder();
        if (cssStyle != null) {
            sb.append(cssStyle.getInlineStyles());
        }
        if (CommonUtils.notEmpty(styleList)) {
            for (CSSStyle style : styleList) {
                sb.append(style.getInlineStyles());
            }
        }
        if (sb.length() > 0) {
            return SAXHelper.addAttrValue(null, STYLE_ATTR, sb.toString());
        }
        return null;
    }
}

package com.publiccms.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.HtmlDocumentFacade;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.publiccms.common.constants.Constants;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;

public class DocToHtmlUtils {

    /**
     * @param file
     * @param picturesManager
     * @return html
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static String docToHtml(File file, PicturesManager picturesManager)
            throws IOException, ParserConfigurationException, TransformerException {
        try (HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(file));) {
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(new StyleOnlyDocumentFacade());
            wordToHtmlConverter.setPicturesManager(picturesManager);
            wordToHtmlConverter.processDocument(wordDocument);
            return documentToHtml(wordToHtmlConverter.getDocument());
        }
    }

    /**
     * @param file
     * @param imageManager
     * @return html
     * @throws IOException
     */
    public static String docxToHtml(File file, ImageManager imageManager) throws IOException {
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(file));) {
            XHTMLOptions options = XHTMLOptions.create().setImageManager(imageManager).setFragment(true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CustomXHTMLConverter.getInstance().convert(document, out, options);
            return HtmlUtils.UNESCAPE_HTML4.translate(new String(out.toByteArray()));
        }
    }

    /**
     * @param htmlDocument
     * @throws TransformerException
     */
    private static String documentToHtml(Document htmlDocument) throws TransformerException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(out);
        DOMSource domSource = new DOMSource(htmlDocument);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, Constants.DEFAULT_CHARSET_NAME);
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        String html = new String(out.toByteArray());
        int index1 = html.indexOf("<body");
        int index2 = html.indexOf("</body>");
        if (-1 < index1 && -1 < index2) {
            html = html.substring(html.indexOf('>', index1) + 1, index2);
        }
        return HtmlUtils.UNESCAPE_HTML4.translate(html);
    }

    /**
     * @param file
     * @param imageManager
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static String excelToHtml(File file, ImageManager imageManager)
            throws FileNotFoundException, IOException, ParserConfigurationException, TransformerException {
        try (Workbook wb = WorkbookFactory.create(new FileInputStream(file));) {
            if (wb instanceof HSSFWorkbook) {
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
                        DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                XHTMLOptions options = XHTMLOptions.create();
                options.setImageManager(imageManager);
                excelToHtmlConverter.setOutputRowNumbers(false);
                excelToHtmlConverter.setOutputHiddenRows(false);
                excelToHtmlConverter.setOutputColumnHeaders(false);
                excelToHtmlConverter.setOutputHiddenColumns(true);
                excelToHtmlConverter.processWorkbook(hWb);
                return documentToHtml(excelToHtmlConverter.getDocument());
            } else {
                return XSSFWorkbookUtils.workbookToHtml(wb);
            }
        }
    }

    static class StyleOnlyDocumentFacade extends HtmlDocumentFacade {

        public StyleOnlyDocumentFacade() throws ParserConfigurationException {
            super(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        }

        @Override
        public void addStyleClass(Element element, String classNamePrefix, String style) {
            String exising = element.getAttribute("style");
            String newStyleValue = CommonUtils.empty(exising) ? style : (exising + (exising.endsWith(";") ? style : ";" + style));
            element.setAttribute("style", newStyleValue);
        }
    }
}

package com.publiccms.common.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.HtmlDocumentFacade;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fit.pdfdom.PDFDomTreeConfig;
import org.fit.pdfdom.resource.HtmlResourceHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.document.CustomPDFDomTree;
import com.publiccms.common.document.CustomXHTMLConverter;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;

public class DocToHtmlUtils {
    public static final Pattern WIDTH_HEIGHT_PATTERN = Pattern
            .compile("^\\d+(\\.\\d+)?(cm|px|em|ex|mm|in|pt|pc|vh|vw|vmin|vmax|ch|rem|%)?");

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
        try (HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(file))) {
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
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(file))) {
            XHTMLOptions options = XHTMLOptions.create().setImageManager(imageManager).setFragment(true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CustomXHTMLConverter.getInstance().convert(document, out, options);
            return HtmlUtils.UNESCAPE_HTML4.translate(new String(out.toByteArray()));
        }
    }

    /**
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public static String pdfToHtml(String filePath, String width, String height) {
        StringBuilder sb = new StringBuilder();
        sb.append("<iframe src=\"").append(filePath);
        if (WIDTH_HEIGHT_PATTERN.matcher(width).matches() && WIDTH_HEIGHT_PATTERN.matcher(height).matches()) {
            sb.append("\" style=\"width:").append(width).append(";height:").append(height);
        }
        sb.append("\" frameborder=\"0\"></iframe>");

        return sb.toString();
    }

    /**
     * @param file
     * @param imageHandler
     * @return
     * @throws IOException
     * @throws ClassCastException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws TransformerException
     */
    public static String pdfToHtml(File file, HtmlResourceHandler imageHandler) throws IOException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, ClassCastException, TransformerException {
        try (PDDocument pdf = PDDocument.load(file)) {
            PDFDomTreeConfig config = PDFDomTreeConfig.createDefaultConfig();
            config.setFontHandler(PDFDomTreeConfig.ignoreResource());
            config.setImageHandler(imageHandler);
            CustomPDFDomTree parser = new CustomPDFDomTree(config);
            return documentToHtml(parser.createDOM(pdf));
        }
    }

    /**
     * @param file
     * @param imageManager
     * @return
     * @throws Exception
     */
    public static String pptToHtml(File file, ImageManager imageManager) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (SlideShow<?, ?> sw = SlideShowFactory.create(new FileInputStream(file))) {
            Dimension pgSize = sw.getPageSize();
            for (Slide<?, ?> slide : sw.getSlides()) {
                BufferedImage img = new BufferedImage(pgSize.width, pgSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgSize.width, pgSize.height));
                slide.draw(graphics);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(img, ImageUtils.FORMAT_NAME_JPG, out);
                imageManager.extract(slide.getSlideNumber() + ".jpg", out.toByteArray());
                sb.append("<p style=\"text-align:center\"><img src=\"").append(imageManager.resolve(null)).append("\" alt=\"")
                        .append(slide.getSlideNumber()).append("\"/></p>");
            }
        }
        return sb.toString();
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
        try (Workbook wb = WorkbookFactory.create(new FileInputStream(file))) {
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

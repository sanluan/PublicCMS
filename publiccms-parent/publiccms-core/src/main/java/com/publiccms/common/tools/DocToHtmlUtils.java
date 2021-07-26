package com.publiccms.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.HtmlDocumentFacade;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;

public class DocToHtmlUtils {

    /**
     * @param wordFile
     * @param picturesManager
     * @return html
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static String docToHtml(File wordFile, PicturesManager picturesManager)
            throws IOException, ParserConfigurationException, TransformerException {
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(wordFile));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(new StyleOnlyDocumentFacade());
        wordToHtmlConverter.setPicturesManager(picturesManager);
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        wordDocument.close();
        String html = new String(out.toByteArray());
        int index1 = html.indexOf("<body");
        int index2 = html.indexOf("</body>");
        if (-1 < index1 && -1 < index2) {
            html = html.substring(html.indexOf('>', index1) + 1, index2);
        }
        return html;
    }

    /**
     * @param wordFile
     * @param imageManager
     * @return html
     * @throws IOException
     */
    public static String docxToHtml(File wordFile, ImageManager imageManager) throws IOException {
        ZipSecureFile.setMinInflateRatio(-1.0d);
        InputStream in = new FileInputStream(wordFile);
        XWPFDocument document = new XWPFDocument(in);
        XHTMLOptions options = XHTMLOptions.create().setImageManager(imageManager).setFragment(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, out, options);
        return new String(out.toByteArray());
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

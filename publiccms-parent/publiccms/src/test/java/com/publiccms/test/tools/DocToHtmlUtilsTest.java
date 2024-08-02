package com.publiccms.test.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.tools.DocToHtmlUtils;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;

@DisplayName("doc to html utils test case")
public class DocToHtmlUtilsTest {
    protected final Log log = LogFactory.getLog(getClass());
    String docxFilepath = "src/test/resources/test/tools/document.docx";
    String docFilepath = "src/test/resources/test/tools/document.doc";

    @Test
    @DisplayName("docx to html")
    void docxToHtml() {
        try {
            String html = DocToHtmlUtils.docxToHtml(new File(docxFilepath), new ImageManager(new File(""), "") {

                @Override
                public void extract(String imagePath, byte[] imageData) throws IOException {
                }

                @Override
                public String resolve(String uri) {
                    return null;
                }
            });
            log.info(html);
            Assertions.assertTrue(html.contains("PublicCMS DocxToHtmlTest"));
        } catch (IOException e) {
        }
    }

    @Test
    @DisplayName("doc to html")
    void docToHtml() {
        try {
            String html = DocToHtmlUtils.docToHtml(new File(docFilepath), new PicturesManager() {

                @Override
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                        float heightInches) {
                    return null;
                }
            });
            log.info(html);
            Assertions.assertTrue(html.contains("PublicCMS DocToHtmlTest"));
        } catch (IOException | ParserConfigurationException | TransformerException e) {
        }
    }
}

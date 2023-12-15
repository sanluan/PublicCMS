package com.publiccms.test.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.tools.CmsFileUtils;

/**
 * BatchTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("File utils test case")
class PdfXssTest {
    protected final Log log = LogFactory.getLog(getClass());
    String filepath = "src/test/resources/test/tools/xss.pdf";

    @Test
    @DisplayName("get suffix")
    void getSuffix() {
        Assertions.assertEquals(".pdf", CmsFileUtils.getSuffix(filepath));
    }

    @Test
    @DisplayName("is file")
    void isFile() {
        Assertions.assertTrue(CmsFileUtils.isFile(filepath));
    }

    @Test
    @DisplayName("file exists")
    void exists() {
        Assertions.assertTrue(CmsFileUtils.exists(filepath));
    }

    @Test
    @DisplayName("is file")
    void isDirectory() {
        Assertions.assertFalse(CmsFileUtils.isDirectory(filepath));
    }

    @Test
    @DisplayName("pdf xss test case")
    void pdfxss() {
        Assertions.assertFalse(CmsFileUtils.isSafe(filepath, CmsFileUtils.getSuffix(filepath)));
    }

}

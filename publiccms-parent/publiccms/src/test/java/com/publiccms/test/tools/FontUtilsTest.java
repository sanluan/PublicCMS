package com.publiccms.test.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.tools.FontUtils;

@DisplayName("font utils test case")
public class FontUtilsTest {
    protected final Log log = LogFactory.getLog(getClass());

    @Test
    @DisplayName("generate font")
    void testFontGenerate() throws IOException {
        File fontFile = new File("src/test/resources/test/tools/Reckoner-1.ttf");
        File outputFile = new File("target/test.ttf");
        List<Character> wordList = FontUtils.sortedCharList("publiccms");
        Map<Character, Character> swapWordMap = FontUtils.swapWordMap(wordList, 5);
        outputFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            FontUtils.generateFont(fontFile, fos, swapWordMap);
            Assertions.assertTrue(outputFile.exists());
        }
        log.info(swapWordMap);
    }
}

package com.publiccms.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.tools.sfnttool.GlyphCoverage;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import com.google.typography.font.tools.subsetter.Subsetter;

/**
 * FontUtils
 * 
 */
public class FontUtils {
    public static Set<Integer> REMOVE_TABLES = Arrays.asList(Tag.GDEF, Tag.GPOS, Tag.GSUB, Tag.kern, Tag.hdmx, Tag.vmtx, Tag.VDMX,
            Tag.LTSH, Tag.DSIG, Tag.vhea, Tag.mort, Tag.morx).stream().collect(Collectors.toSet());
    public static List<CMapTable.CMapId> CMAPIDS = Arrays.asList(CMapTable.CMapId.WINDOWS_BMP);

    private FontUtils() {
    }

    public static List<Character> sortedCharList(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).collect(Collectors.toList());
    }

    public static Map<Character, Character> swapWordMap(List<Character> sortedCharList, int size) {
        List<Character> copy;
        if (sortedCharList.size() > size) {
            copy = Arrays.asList(sortedCharList.subList(0, size).toArray(new Character[0]));
        } else {
            copy = Arrays.asList(sortedCharList.toArray(new Character[0]));
        }
        Collections.shuffle(copy);
        int i = 0;
        Map<Character, Character> swapMap = new HashMap<>();
        for (Character c : copy) {
            swapMap.put(sortedCharList.get(i++), c);
        }
        return swapMap;
    }

    public static Font loadFont(File fontFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(fontFile);) {
            Font[] fontArray = FontFactory.loadFonts(fis);
            return fontArray[0];
        }
    }

    public static String generateFontData(Font font, Map<Character, Character> swapWordMap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        generateFont(font, byteArrayOutputStream, swapWordMap);
        byteArrayOutputStream.close();
        return CommonUtils.joinString("data:font/ttf;charset=utf-8;base64,",
                VerificationUtils.base64Encode(byteArrayOutputStream.toByteArray()));
    }

    public static String generateFontData(File fontFile, Map<Character, Character> swapWordMap) throws IOException {
        return generateFontData(loadFont(fontFile), swapWordMap);
    }

    public static void generateFont(File fontFile, OutputStream outputStream, Map<Character, Character> swapWordMap)
            throws IOException {
        generateFont(loadFont(fontFile), outputStream, swapWordMap);
    }

    public static void generateFont(Font font, OutputStream outputStream, Map<Character, Character> swapWordMap)
            throws IOException {
        Subsetter subsetter = new RenumberingSubsetter(font, swapWordMap);
        subsetter.setCMaps(CMAPIDS, 1);
        List<Integer> glyphs = GlyphCoverage.getGlyphCoverage(font, swapWordMap);
        subsetter.setGlyphs(glyphs);
        subsetter.setRemoveTables(REMOVE_TABLES);
        FontFactory.serializeFont(subsetter.subset().build(), outputStream);
    }

}

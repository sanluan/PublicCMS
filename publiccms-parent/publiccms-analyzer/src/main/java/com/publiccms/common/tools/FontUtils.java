package com.publiccms.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    private FontUtils() {
    }

    public static Map<Character, Character> swapWordMap(File fontFile, File outputFile, String input, int size)
            throws IOException {
        List<Character> sortedCharList = generateSortedCharList(input);
        Map<Character, Character> swapWordMap = generateSwapWordMap(sortedCharList, size);
        generateFontFile(fontFile, outputFile, swapWordMap);
        return swapWordMap;
    }

    public static List<Character> generateSortedCharList(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).collect(Collectors.toList());
    }

    public static Map<Character, Character> generateSwapWordMap(List<Character> sortedCharList, int size) {
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

    public static void generateFontFile(File fontFile, File outputFile, Map<Character, Character> swapWordMap)
            throws IOException {
        outputFile.getParentFile().mkdirs();
        try (FileInputStream fis = new FileInputStream(fontFile); FileOutputStream fos = new FileOutputStream(outputFile);) {
            Font[] fontArray = FontFactory.loadFonts(fis);
            Font font = fontArray[0];
            Subsetter subsetter = new RenumberingSubsetter(font, swapWordMap);
            List<CMapTable.CMapId> cmapIds = new ArrayList<>();
            cmapIds.add(CMapTable.CMapId.WINDOWS_BMP);
            subsetter.setCMaps(cmapIds, 1);
            List<Integer> glyphs = GlyphCoverage.getGlyphCoverage(font, swapWordMap);
            subsetter.setGlyphs(glyphs);
            Set<Integer> removeTables = new HashSet<>();
            removeTables.add(Tag.GDEF);
            removeTables.add(Tag.GPOS);
            removeTables.add(Tag.GSUB);
            removeTables.add(Tag.kern);
            removeTables.add(Tag.hdmx);
            removeTables.add(Tag.vmtx);
            removeTables.add(Tag.VDMX);
            removeTables.add(Tag.LTSH);
            removeTables.add(Tag.DSIG);
            removeTables.add(Tag.vhea);
            removeTables.add(Tag.mort);
            removeTables.add(Tag.morx);
            subsetter.setRemoveTables(removeTables);
            Font newFont = subsetter.subset().build();
            FontFactory.serializeFont(newFont, fos);
        }
    }
}

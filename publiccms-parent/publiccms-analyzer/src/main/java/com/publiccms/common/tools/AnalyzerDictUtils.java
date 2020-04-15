package com.publiccms.common.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.hhmm.DictionaryReloader;

public class AnalyzerDictUtils {
    public static final String TXT_DICT = "dict.txt";
    public static final String DIR_DICT = "/dict/";

    public static void generate(String newCoreDir, Map<String, Integer> wordMap)
            throws FileNotFoundException, ClassNotFoundException, IOException {
        Map<String, Map<String, Integer>> cnTFsMap = new HashMap<>();
        Map<String, Integer> deliFreqsMap = SmartcnDictUtils.defaultDelimiterFreqsMap;
        SmartcnDictUtils.readFromCoreMem(SmartChineseAnalyzer.class.getResourceAsStream("hhmm/coredict.mem"), cnTFsMap,
                deliFreqsMap);
        SmartcnDictUtils.mergeTFsMap(cnTFsMap, wordMap);
        SmartcnDictUtils.create(newCoreDir + DictionaryReloader.DICT_COREDICT, SmartcnDictUtils.TYPE_CORE, cnTFsMap,
                deliFreqsMap);
        SmartcnDictUtils.create(newCoreDir + DictionaryReloader.DICT_BIGRAMDICT, SmartcnDictUtils.TYPE_BIGRAM, cnTFsMap,
                deliFreqsMap);
        File file = new File(newCoreDir + DictionaryReloader.MEM_COREDICT);
        if (file.exists()) {
            file.delete();
        }
        File file2 = new File(newCoreDir + DictionaryReloader.MEM_BIGRAMDICT);
        if (file2.exists()) {
            file2.delete();
        }
    }
}
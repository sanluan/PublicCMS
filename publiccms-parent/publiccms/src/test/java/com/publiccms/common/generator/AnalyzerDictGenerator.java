package com.publiccms.common.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.publiccms.common.tools.SmartcnDictUtils;
import org.apache.lucene.analysis.cn.smart.hhmm.DictionaryReloader;

public class AnalyzerDictGenerator {

    public static void main(String[] args) {
        String analyzeStr = "你好天津黑核科技有限公司";
        String dirPath = "src/test/resources/dict";
        String mydict = "mydict.txt";
        print(analyzeStr);
        try {
            Map<String, Integer> wordMap = new HashMap<>();
            File mydictFile = new File(dirPath + "/" + mydict);
            if (mydictFile.exists()) {
                for (String word : FileUtils.readLines(mydictFile)) {
                    if (!word.startsWith("#")) {
                        wordMap.put(word, 10);
                    }
                }
            }
            generate(dirPath, wordMap);
            DictionaryReloader.reload(dirPath);
        } catch (IOException | ClassNotFoundException e1) {
        }
        print(analyzeStr);

    }

    public static void print(String analyzeStr) {
        try (Analyzer analyzer = new SmartChineseAnalyzer();
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(analyzeStr))) {
            CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                System.out.print(attr.toString() + ",");
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void generate(String newCoreDir, Map<String, Integer> wordMap)
            throws FileNotFoundException, ClassNotFoundException, IOException {
        new File(newCoreDir).mkdirs();
        Map<String, Map<String, Integer>> cnTFsMap = new HashMap<>();
        Map<String, Integer> deliFreqsMap = SmartcnDictUtils.defaultDelimiterFreqsMap;
        SmartcnDictUtils.readFromCoreMem(SmartChineseAnalyzer.class.getResourceAsStream("hhmm/coredict.mem"), cnTFsMap,
                deliFreqsMap);
        SmartcnDictUtils.mergeTFsMap(cnTFsMap, wordMap);
        SmartcnDictUtils.create(newCoreDir + "/coredict.dct", SmartcnDictUtils.TYPE_CORE, cnTFsMap, deliFreqsMap);
        SmartcnDictUtils.create(newCoreDir + "/bigramdict.dct", SmartcnDictUtils.TYPE_BIGRAM, cnTFsMap, deliFreqsMap);
        File file = new File(newCoreDir + "/coredict.mem");
        if (file.exists()) {
            file.delete();
        }
        File file2 = new File(newCoreDir + "/bigramdict.mem");
        if (file2.exists()) {
            file2.delete();
        }
    }
}
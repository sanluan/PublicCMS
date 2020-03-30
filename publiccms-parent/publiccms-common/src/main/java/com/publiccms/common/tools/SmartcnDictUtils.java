package com.publiccms.common.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 原始dict文件工具类 https://github.com/Kerwin23/smartcn-dict
 * 
 * @author Kerwin
 *
 */
public class SmartcnDictUtils {
    private static final String CHARSET_GB2312 = "GB2312";
    public static final int GB2312_FIRST_CHAR = 1410;
    public static final int GB2312_CHAR_NUM = 87 * 94;
    public static final int CHAR_NUM_IN_FILE = 6768;
    public final static String TYPE_CORE = "core";
    public final static String TYPE_BIGRAM = "bigram";
    public final static Map<String, Integer> defaultDelimiterFreqsMap = new HashMap<>();

    static {
        defaultDelimiterFreqsMap.put("，", 20);
        defaultDelimiterFreqsMap.put("“", 20);
        defaultDelimiterFreqsMap.put("”", 20);
        defaultDelimiterFreqsMap.put("：", 20);
        defaultDelimiterFreqsMap.put("。", 20);
    }

    public static String readCnTerm(String src) {
        if (null != src) {
            String tmpTerm = src.replaceAll(" ", "");
            int subIndex = -1;
            int len = tmpTerm.length();
            int firstCcid = GB2312_FIRST_CHAR;
            int lastCcid = GB2312_FIRST_CHAR + GB2312_CHAR_NUM;
            for (int i = 0; i < len; i++) {
                char ch = tmpTerm.charAt(i);
                int ccid = getGB2312Id(ch);
                if (ccid >= firstCcid && ccid <= lastCcid) {
                    subIndex = i;
                    break;
                }
            }
            if (subIndex != -1) {
                return tmpTerm.substring(subIndex);
            }
        }
        return null;
    }

    /**
     * 读取已有词库mem文件
     * 
     * @param inputStream
     * @param charTermFreqsMap
     * @param delimiterFreqsMap
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readFromCoreMem(InputStream inputStream, Map<String, Map<String, Integer>> charTermFreqsMap,
            Map<String, Integer> delimiterFreqsMap) throws IOException, ClassNotFoundException {
        try (ObjectInputStream oiStream = new ObjectInputStream(inputStream)) {
            oiStream.readObject();
            oiStream.readObject();
            char[][][] wordItemCharArrayTable = (char[][][]) oiStream.readObject();
            int[][] wordItemFrequencyTable = (int[][]) oiStream.readObject();
            readCoreMemFromArrays(wordItemCharArrayTable, wordItemFrequencyTable, charTermFreqsMap, delimiterFreqsMap);
        }
    }

    /**
     * 增加新的分词
     * 
     * @param tfsMap
     * @param source
     */
    public static void mergeTFsMap(Map<String, Map<String, Integer>> tfsMap, Map<String, Integer> source) {
        Map<String, Map<String, Integer>> tempTfsMap = transSource(source);
        merge(tfsMap, tempTfsMap, tfsMap.keySet());
        merge(tfsMap, tempTfsMap, tempTfsMap.keySet());
    }

    /**
     * 创建词库文件
     * 
     * @param filePath
     * @param type
     * @param charTFsMap
     * @param delimiterFreqsMap
     */
    public static void create(String filePath, String type, Map<String, Map<String, Integer>> charTFsMap,
            Map<String, Integer> delimiterFreqsMap) {
        try (OutputStream oStream = new FileOutputStream(filePath)) {
            for (int i = GB2312_FIRST_CHAR; i < GB2312_FIRST_CHAR + CHAR_NUM_IN_FILE; i++) {
                if (3755 + GB2312_FIRST_CHAR == i && TYPE_CORE.equals(type)) {
                    writeDelimiters(oStream, delimiterFreqsMap);
                    continue;
                }
                String cc = getCCByGB2312Id(i);
                Map<String, Integer> tfs = charTFsMap.get(cc);
                if (null == tfs || tfs.isEmpty()) {
                    writeEmpty(oStream);
                } else {
                    writeTFs(oStream, tfs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Map<String, Integer>> transSource(Map<String, Integer> source) {
        Set<String> keys = source.keySet();
        Map<String, Map<String, Integer>> charTermFreqsMap = new HashMap<>();
        for (String key : keys) {
            Integer freq = source.get(key);
            String ch = key.substring(0, 1);
            Map<String, Integer> tfs = charTermFreqsMap.get(ch);
            if (null == tfs) {
                tfs = new HashMap<>();
            }
            tfs.put(key.substring(1), freq);
            charTermFreqsMap.put(ch, tfs);
        }
        return charTermFreqsMap;
    }

    private static void readCoreMemFromArrays(char[][][] wordItemCharArrayTable, int[][] wordItemFrequencyTable,
            Map<String, Map<String, Integer>> charTermFreqsMap, Map<String, Integer> delimiterFreqsMap) {
        int length = wordItemCharArrayTable.length;
        for (int i = 0; i < length; i++) {
            char[][] termsArray = wordItemCharArrayTable[i];
            if (null != termsArray) {
                String cc = getCCByGB2312Id(i);
                if (i < GB2312_FIRST_CHAR) {
                    readCoreMemDelimeter(cc, termsArray, wordItemFrequencyTable[i], delimiterFreqsMap);
                } else {
                    readCoreMemTerms(cc, termsArray, wordItemFrequencyTable[i], charTermFreqsMap);
                }
            }
        }
    }

    private static void readCoreMemTerms(String cc, char[][] termsArray, int[] freqArray,
            Map<String, Map<String, Integer>> charTermFreqsMap) {
        Map<String, Integer> tfs = new HashMap<>();
        int cnt = termsArray.length;
        for (int i = 0; i < cnt; i++) {
            char[] termArray = termsArray[i];
            if (null != termArray) {
                tfs.put(new String(termArray), freqArray[i]);
            }
        }
        charTermFreqsMap.put(cc, tfs);
    }

    private static void readCoreMemDelimeter(String cc, char[][] termsArray, int[] freqArray,
            Map<String, Integer> delimiterFreqsMap) {
        int freq = 0;
        int cnt = termsArray.length;
        for (int i = 0; i < cnt; i++) {
            char[] termArray = termsArray[i];
            if (null != termArray) {
                freq += freqArray[i];
            }
        }
        delimiterFreqsMap.put(cc, freq);
    }

    private static void merge(Map<String, Map<String, Integer>> tfsMap, Map<String, Map<String, Integer>> tempTfsMap,
            Set<String> keys) {
        for (String key : keys) {
            Map<String, Integer> tfs = tfsMap.get(key);
            if (null == tfs) {
                tfs = new HashMap<>();
                tfsMap.put(key, tfs);
            }
            Map<String, Integer> temp = tempTfsMap.get(key);
            if (null != temp) {
                tfs.putAll(temp);
            }
        }
    }

    private static void writeDelimiters(OutputStream oStream, Map<String, Integer> delimiterFreqsMap) throws Exception {
        int cnt = delimiterFreqsMap.size();
        writeInt(oStream, cnt);
        Set<String> keys = delimiterFreqsMap.keySet();
        for (String key : keys) {
            int freq = delimiterFreqsMap.get(key);
            writeInt(oStream, freq);
            byte[] deliBs = key.getBytes(CHARSET_GB2312);
            int len = deliBs.length;
            writeInt(oStream, len);
            writeInt(oStream, 0);
            oStream.write(deliBs);
        }
    }

    private static void writeTFs(OutputStream oStream, Map<String, Integer> tfs) throws Exception {
        int cnt = tfs.size();
        writeInt(oStream, cnt);
        for (Entry<String, Integer> tf : tfs.entrySet()) {
            int freq = tf.getValue();
            writeInt(oStream, freq);
            byte[] termBs = tf.getKey().getBytes(CHARSET_GB2312);
            int len = termBs.length;
            writeInt(oStream, len);
            writeInt(oStream, 0);
            oStream.write(termBs);
        }
    }

    private static void writeEmpty(OutputStream oStream) throws Exception {
        writeInt(oStream, 0);
    }

    private static void writeInt(OutputStream oStream, int i) throws Exception {
        byte[] bytes = intToLEBytes(i);
        oStream.write(bytes);
    }

    private static byte[] intToLEBytes(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(i);
        return buffer.array();
    }

    private static String getCCByGB2312Id(int ccid) {
        if (0 > ccid || GB2312_CHAR_NUM < ccid) {
            return "";
        }
        int cc1 = ccid / 94 + 161;
        int cc2 = ccid % 94 + 161;
        byte[] buffer = new byte[2];
        buffer[0] = (byte) cc1;
        buffer[1] = (byte) cc2;
        try {
            String cchar = new String(buffer, CHARSET_GB2312);
            return cchar;
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes(CHARSET_GB2312);
            if (buffer.length != 2) {
                return -1;
            }
            int b0 = (buffer[0] & 0x0FF) - 161;
            int b1 = (buffer[1] & 0x0FF) - 161;
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

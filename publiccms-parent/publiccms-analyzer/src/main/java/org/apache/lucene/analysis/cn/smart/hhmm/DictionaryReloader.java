package org.apache.lucene.analysis.cn.smart.hhmm;

import java.io.File;
import java.lang.reflect.Method;

/**
 * https://github.com/Kerwin23/smartcn-dict
 * 
 * @author Kerwin
 *
 */
public class DictionaryReloader {
    public static final String DICT_COREDICT = "/coredict.dct";
    public static final String DICT_BIGRAMDICT = "/bigramdict.dct";
    public static final String MEM_COREDICT = "/coredict.mem";
    public static final String MEM_BIGRAMDICT = "/bigramdict.mem";

    /**
     * @param dctroot
     */
    public synchronized static void reload(String dctroot) {
        if (new File(dctroot + DICT_COREDICT).exists() || new File(dctroot + MEM_COREDICT).exists()) {
            WordDictionary dict = WordDictionary.getInstance();
            dict.load(dctroot);
        }
        if (new File(dctroot + DICT_BIGRAMDICT).exists() || new File(dctroot + MEM_BIGRAMDICT).exists()) {
            BigramDictionary dict = BigramDictionary.getInstance();
            try {
                Method method = BigramDictionary.class.getDeclaredMethod("load", String.class);
                method.setAccessible(true);
                method.invoke(dict, dctroot);
            } catch (Exception e) {
            }
        }
    }
}

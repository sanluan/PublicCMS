package org.apache.lucene.analysis.cn.smart.hhmm;

import java.lang.reflect.Method;

/**
 * https://github.com/Kerwin23/smartcn-dict
 * 
 * @author Kerwin
 *
 */
public class DictionaryReloader {

    public final synchronized static void reload(String dctroot) {
        reloadCoreMem(dctroot);
        reloadBigramMem(dctroot);
    }

    private final static void reloadCoreMem(String dctroot) {
        WordDictionary dict = WordDictionary.getInstance();
        dict.load(dctroot);
    }

    private final static void reloadBigramMem(String dctroot) {
        BigramDictionary dict = BigramDictionary.getInstance();
        try {
            Method method = BigramDictionary.class.getDeclaredMethod("load", String.class);
            method.setAccessible(true);
            method.invoke(dict, dctroot);
        } catch (Exception e) {
        }
    }
}

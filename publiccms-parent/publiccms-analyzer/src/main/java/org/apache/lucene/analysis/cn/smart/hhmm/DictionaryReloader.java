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

    public synchronized static void reload(String dctroot) {
        if (new File(dctroot + "/coredict.dct").exists() || new File(dctroot + "/coredict.mem").exists()) {
            WordDictionary dict = WordDictionary.getInstance();
            dict.load(dctroot);
        }
        if (new File(dctroot + "/bigramdict.dct").exists() || new File(dctroot + "/bigramdict.mem").exists()) {
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

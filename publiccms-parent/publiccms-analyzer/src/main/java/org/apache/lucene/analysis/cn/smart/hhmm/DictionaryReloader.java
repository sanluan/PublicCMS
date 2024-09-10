package org.apache.lucene.analysis.cn.smart.hhmm;

import java.io.File;
import java.lang.reflect.Method;

import com.publiccms.common.tools.CommonUtils;

/**
 * https://github.com/Kerwin23/smartcn-dict
 * 
 * @author Kerwin
 *
 */
public class DictionaryReloader {
    private DictionaryReloader() {
    }

    public static final String DICT_COREDICT = "/coredict.dct";
    public static final String DICT_BIGRAMDICT = "/bigramdict.dct";
    public static final String MEM_COREDICT = "/coredict.mem";
    public static final String MEM_BIGRAMDICT = "/bigramdict.mem";

    /**
     * @param dctroot
     */
    public static synchronized void reload(String dctroot) {
        if (new File(CommonUtils.joinString(dctroot, DICT_COREDICT)).exists()
                || new File(CommonUtils.joinString(dctroot, MEM_COREDICT)).exists()) {
            WordDictionary dict = WordDictionary.getInstance();
            dict.load(dctroot);
        }
        if (new File(CommonUtils.joinString(dctroot, DICT_BIGRAMDICT)).exists()
                || new File(CommonUtils.joinString(dctroot, MEM_BIGRAMDICT)).exists()) {
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

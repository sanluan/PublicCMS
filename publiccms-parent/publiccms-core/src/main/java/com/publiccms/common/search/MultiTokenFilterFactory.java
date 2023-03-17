package com.publiccms.common.search;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import com.publiccms.common.tools.CommonUtils;

public class MultiTokenFilterFactory extends TokenFilterFactory implements ResourceLoaderAware {
    private static String name;
    private static Map<String, String> args;
    /**
     * 
     */
    public TokenFilterFactory tokenFilterFactory;
    protected final Log log = LogFactory.getLog(getClass());

    public MultiTokenFilterFactory(Map<String, String> args) {
        super(args);
        Set<String> set = availableTokenFilters();
        String currentName = name;
        if (!set.contains(currentName)) {
            currentName = "standard";
        }
        args.putAll(MultiTokenFilterFactory.args);
        tokenFilterFactory = forName(currentName, args);
        log.info(CommonUtils.joinString(currentName, " token filter factory created,available token filters:", set.toString()));
    }

    @Override
    public TokenStream create(TokenStream input) {
        return tokenFilterFactory.create(input);
    }

    /**
     * @param name
     * @param args
     */
    public static void init(String name, Map<String, String> args) {
        MultiTokenFilterFactory.name = name;
        MultiTokenFilterFactory.args = args;
    }

    @Override
    public void inform(ResourceLoader loader) throws IOException {
        if (tokenFilterFactory instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) tokenFilterFactory).inform(loader);
        }
    }

    /**
     * @return the name
     */
    public static String getName() {
        return name;
    }
}
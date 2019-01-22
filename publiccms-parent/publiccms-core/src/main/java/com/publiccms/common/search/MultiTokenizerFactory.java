package com.publiccms.common.search;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

/**
 *
 * MultiTokenizerFactory
 * 
 */
public class MultiTokenizerFactory extends TokenizerFactory implements ResourceLoaderAware {
    private static String name;
    private static Map<String, String> args;
    /**
     * 
     */
    public TokenizerFactory tokenizerFactory;
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param args
     */
    public MultiTokenizerFactory(Map<String, String> args) {
        super(args);
        Set<String> set = availableTokenizers();
        if (!set.contains(name)) {
            name = "standard";
        }
        args.putAll(MultiTokenizerFactory.args);
        tokenizerFactory = forName(name, args);
        log.info(new StringBuilder().append(name).append(" tokenizer factory created,available tokenizers:").append(set)
                .toString());
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return tokenizerFactory.create(factory);
    }
    

    @Override
    public void inform(ResourceLoader loader) throws IOException {
        if (null != tokenizerFactory && tokenizerFactory instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) tokenizerFactory).inform(loader);
        }
    }

    /**
     * @param name
     * @param args 
     */
    public static void init(String name, Map<String, String> args) {
        MultiTokenizerFactory.name = name;
        MultiTokenizerFactory.args = args;
    }
}
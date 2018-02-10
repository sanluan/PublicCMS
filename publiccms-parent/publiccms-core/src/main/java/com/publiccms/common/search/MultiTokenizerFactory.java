package com.publiccms.common.search;


import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

/**
 *
 * MultiTokenizerFactory
 * 
 */
public class MultiTokenizerFactory extends TokenizerFactory {
    private static String name;
    /**
     * 
     */
    public TokenizerFactory tokenizerFactory;
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param args
     */
    public MultiTokenizerFactory(Map<String, String> args) {
        this(args, name);
    }

    /**
     * @param args
     * @param name
     */
    public MultiTokenizerFactory(Map<String, String> args, String name) {
        super(args);
        Set<String> set = availableTokenizers();
        if (!set.contains(name)) {
            name = "standard";
        }
        tokenizerFactory = forName(name, args);
        log.info(new StringBuilder().append(name).append(" tokenizer factory created,available tokenizers:").append(set).toString());
        if (!args.isEmpty()) {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown parameters: ").append(args).toString());
        }
    }

    public Tokenizer create(AttributeFactory factory) {
        return tokenizerFactory.create(factory);
    }

    /**
     * @param name
     */
    public static void setName(String name) {
        MultiTokenizerFactory.name = name;
    }
}
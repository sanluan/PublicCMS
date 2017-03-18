package com.publiccms.common.analyzer;

import static org.apache.commons.logging.LogFactory.getLog;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
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
    public TokenizerFactory tokenizerFactory;
    protected final Log log = getLog(getClass());

    public MultiTokenizerFactory(Map<String, String> args) {
        this(args, name);
    }

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

    public static void setName(String name) {
        MultiTokenizerFactory.name = name;
    }
}
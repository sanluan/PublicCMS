package com.publiccms.common.search;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class CmsLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer(CmsContentAttributeBinder.ANALYZER_NAME).custom().tokenizer(MultiTokenizerFactory.class).tokenFilter(MultiTokenFilterFactory.class);
    }
}
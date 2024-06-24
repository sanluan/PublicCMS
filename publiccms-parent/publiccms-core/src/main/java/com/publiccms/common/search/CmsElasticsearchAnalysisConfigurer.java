package com.publiccms.common.search;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class CmsElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer(CmsContentAttributeBinder.ANALYZER_NAME).custom().tokenizer(MultiTokenizerFactory.getName())
                .tokenFilters(MultiTokenFilterFactory.getName());
    }
}

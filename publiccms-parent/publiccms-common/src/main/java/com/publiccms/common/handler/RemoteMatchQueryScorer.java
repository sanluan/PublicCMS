package com.publiccms.common.handler;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.WeightedSpanTermExtractor;

public class RemoteMatchQueryScorer extends QueryScorer {
    private Analyzer analyzer;

    public RemoteMatchQueryScorer(Analyzer analyzer, Query query, String field) {
        super(query, field);
        this.analyzer = analyzer;
    }

    public RemoteMatchQueryScorer(Analyzer analyzer, Query query, String field, String defaultField) {
        super(query, null, field, defaultField);
        this.analyzer = analyzer;
    }

    @Override
    protected WeightedSpanTermExtractor newTermExtractor(String defaultField) {
        return defaultField == null ? new RemoteMatchQueryWeightedSpanTermExtractor(analyzer)
                : new RemoteMatchQueryWeightedSpanTermExtractor(analyzer, defaultField);
    }
}

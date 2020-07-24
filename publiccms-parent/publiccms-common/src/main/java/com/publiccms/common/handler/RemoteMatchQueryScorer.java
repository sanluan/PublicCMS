package com.publiccms.common.handler;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.WeightedSpanTermExtractor;

public class RemoteMatchQueryScorer extends QueryScorer {

    public RemoteMatchQueryScorer(Query query, String field) {
        super(query, field);
    }

    public RemoteMatchQueryScorer(Query query, String field, String defaultField) {
        super(query, null, field, defaultField);
    }

    @Override
    protected WeightedSpanTermExtractor newTermExtractor(String defaultField) {
        return defaultField == null ? new RemoteMatchQueryWeightedSpanTermExtractor()
                : new RemoteMatchQueryWeightedSpanTermExtractor(defaultField);
    }
}

package com.publiccms.common.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.WeightedSpanTerm;
import org.apache.lucene.search.highlight.WeightedSpanTermExtractor;
import org.hibernate.search.query.dsl.impl.RemoteMatchQuery;

public class RemoteMatchQueryWeightedSpanTermExtractor extends WeightedSpanTermExtractor {
    public RemoteMatchQueryWeightedSpanTermExtractor(String defaultField) {
        super(defaultField);
    }

    public RemoteMatchQueryWeightedSpanTermExtractor() {
    }

    @Override
    protected void extract(Query query, float boost, Map<String, WeightedSpanTerm> terms) throws IOException {
        if (query instanceof RemoteMatchQuery) {
            RemoteMatchQuery queryTerm = (RemoteMatchQuery) query;
            if (fieldNameComparator(queryTerm.getField())) {
                WeightedSpanTerm weightedSpanTerm = new WeightedSpanTerm(boost, queryTerm.getSearchTerms());
                terms.put(queryTerm.getSearchTerms(), weightedSpanTerm);
            }
        } else {
            super.extract(query, boost, terms);
        }
    }
}

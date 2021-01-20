package com.publiccms.common.handler;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.WeightedSpanTerm;
import org.apache.lucene.search.highlight.WeightedSpanTermExtractor;
import org.hibernate.search.query.dsl.impl.RemoteMatchQuery;

public class RemoteMatchQueryWeightedSpanTermExtractor extends WeightedSpanTermExtractor {
    private Analyzer analyzer;

    public RemoteMatchQueryWeightedSpanTermExtractor(Analyzer analyzer, String defaultField) {
        super(defaultField);
        this.analyzer = analyzer;
    }

    public RemoteMatchQueryWeightedSpanTermExtractor(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    protected void extract(Query query, float boost, Map<String, WeightedSpanTerm> terms) throws IOException {
        if (query instanceof RemoteMatchQuery) {
            RemoteMatchQuery queryTerm = (RemoteMatchQuery) query;
            if (fieldNameComparator(queryTerm.getField())) {
                try (StringReader stringReader = new StringReader(queryTerm.getSearchTerms());
                        TokenStream tokenStream = analyzer.tokenStream(queryTerm.getField(), stringReader)) {
                    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        String term = charTermAttribute.toString();
                        WeightedSpanTerm weightedSpanTerm = new WeightedSpanTerm(boost, term);
                        terms.put(term, weightedSpanTerm);
                    }
                    tokenStream.end();
                } catch (IOException e) {
                }

            }
        } else {
            super.extract(query, boost, terms);
        }
    }
}

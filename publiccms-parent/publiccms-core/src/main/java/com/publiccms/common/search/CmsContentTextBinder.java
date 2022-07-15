package com.publiccms.common.search;

import java.math.BigDecimal;

import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.engine.backend.types.IndexFieldType;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;

import com.publiccms.entities.cms.CmsContent;

public class CmsContentTextBinder implements TypeBinder {
    public static final String ANALYZER_NAME = "cms";

    @Override
    public void bind(TypeBindingContext context) {
        context.dependencies().use("id");
        IndexSchemaElement schemaElement = context.indexSchemaElement();

        IndexFieldType<String> textFieldType = context.typeFactory().asString().analyzer(ANALYZER_NAME).toIndexFieldType();
        IndexFieldType<BigDecimal> bigDecimalFieldType = context.typeFactory().asBigDecimal().decimalScale(2).toIndexFieldType();

        IndexFieldReference<String> textField = schemaElement.field("text", textFieldType).toReference();
        IndexFieldReference<String> filesField = schemaElement.field("files", textFieldType).toReference();
        IndexFieldReference<String> productsField = schemaElement.field("products", textFieldType).toReference();

        IndexFieldReference<BigDecimal> minPriceField = schemaElement.field("minPrice", bigDecimalFieldType).toReference();
        IndexFieldReference<BigDecimal> maxPriceField = schemaElement.field("maxPrice", bigDecimalFieldType).toReference();

        context.bridge(CmsContent.class,
                new CmsContentTextBridge(textField, filesField, productsField, minPriceField, maxPriceField));
    }

}

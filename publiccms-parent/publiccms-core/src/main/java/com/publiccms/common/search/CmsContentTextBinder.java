package com.publiccms.common.search;

import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;

import com.publiccms.entities.cms.CmsContent;

public class CmsContentTextBinder implements TypeBinder {

    @Override
    public void bind(TypeBindingContext context) {
        context.dependencies().use("id");
        IndexFieldReference<String> textField = context.indexSchemaElement().field("text", f -> f.asString().analyzer("cms"))
                .toReference();
        context.bridge(CmsContent.class, new CmsContentTextBridge(textField));
    }

}

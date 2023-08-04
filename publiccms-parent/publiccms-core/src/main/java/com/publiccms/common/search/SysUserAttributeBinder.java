package com.publiccms.common.search;

import org.hibernate.search.engine.backend.analysis.AnalyzerNames;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaObjectField;
import org.hibernate.search.engine.backend.types.IndexFieldType;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;

import com.publiccms.entities.sys.SysUser;

public class SysUserAttributeBinder implements TypeBinder {
    public static final String EXTEND_OBJECT_NAME = "extend";
    public static final String ANALYZER_NAME = "cms";

    @Override
    public void bind(TypeBindingContext context) {
        context.dependencies().use("id");
        IndexSchemaElement schemaElement = context.indexSchemaElement();

        IndexFieldType<String> textFieldType = context.typeFactory().asString().projectable(Projectable.NO)
                .analyzer(ANALYZER_NAME).toIndexFieldType();
        IndexFieldType<String> dictionaryFieldType = context.typeFactory().asString().analyzer(AnalyzerNames.WHITESPACE)
                .toIndexFieldType();

        IndexFieldReference<String> textField = schemaElement.field("text", textFieldType).toReference();
        IndexFieldReference<String> dictionaryValuesField = schemaElement.field("dictionaryValues", dictionaryFieldType)
                .toReference();
        IndexFieldReference<String> certificationIdsField = schemaElement.field("certificationIds", dictionaryFieldType).toReference();

        IndexSchemaObjectField extendField = schemaElement.objectField(EXTEND_OBJECT_NAME);
        extendField.fieldTemplate("template", textFieldType);

        context.bridge(SysUser.class,
                new SysUserAttributeBridge(textField, dictionaryValuesField, certificationIdsField, extendField.toReference()));
    }

}

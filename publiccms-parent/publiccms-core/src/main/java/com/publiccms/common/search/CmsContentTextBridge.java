package com.publiccms.common.search;

import java.math.BigDecimal;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.component.BeanComponent;

public class CmsContentTextBridge implements TypeBridge<CmsContent> {
    private final IndexFieldReference<String> textField;
    private final IndexFieldReference<String> filesField;
    private final IndexFieldReference<String> productsField;
    private final IndexFieldReference<BigDecimal> minPriceField;
    private final IndexFieldReference<BigDecimal> maxPriceField;

    public CmsContentTextBridge(IndexFieldReference<String> textField, IndexFieldReference<String> filesField,
            IndexFieldReference<String> productsField, IndexFieldReference<BigDecimal> minPriceField,
            IndexFieldReference<BigDecimal> maxPriceField) {
        this.textField = textField;
        this.filesField = filesField;
        this.productsField = productsField;
        this.minPriceField = minPriceField;
        this.maxPriceField = maxPriceField;
    }

    @Override
    public void write(DocumentElement target, CmsContent bridgedElement, TypeBridgeWriteContext context) {
        CmsContentAttribute attribute = BeanComponent.getContentAttributeService().getEntity(bridgedElement.getId());
        target.addValue(this.textField, null == attribute ? null : attribute.getSearchText());
        target.addValue(this.filesField, null == attribute ? null : attribute.getFilesText());
        target.addValue(this.productsField, null == attribute ? null : attribute.getProductsText());
        target.addValue(this.minPriceField, null == attribute ? null : attribute.getMinPrice());
        target.addValue(this.maxPriceField, null == attribute ? null : attribute.getMaxPrice());
    }
}
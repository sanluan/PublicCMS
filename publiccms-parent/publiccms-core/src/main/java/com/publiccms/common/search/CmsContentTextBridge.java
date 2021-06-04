package com.publiccms.common.search;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.component.BeanComponent;

public class CmsContentTextBridge implements TypeBridge<CmsContent> {
    private final IndexFieldReference<String> field;

    public CmsContentTextBridge(IndexFieldReference<String> field) {
        this.field = field;
    }

    @Override
    public void write(DocumentElement target, CmsContent bridgedElement, TypeBridgeWriteContext context) {
        CmsContentAttribute bridgedElementAttribute = BeanComponent.getContentAttributeService()
                .getEntity(bridgedElement.getId());
        target.addValue(this.field, bridgedElementAttribute.getSearchText());
    }
}
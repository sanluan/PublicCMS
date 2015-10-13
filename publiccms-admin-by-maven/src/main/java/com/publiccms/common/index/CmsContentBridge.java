package com.publiccms.common.index;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

public class CmsContentBridge implements FieldBridge {
    public static CmsContentAttributeService contentAttributeService;

    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        CmsContent content = (CmsContent) value;
        CmsContentAttribute entity = contentAttributeService.getEntity(content.getId());
        if (null != entity) {
            luceneOptions.addFieldToDocument(name+".text", entity.getText(), document);
        }
    }
}
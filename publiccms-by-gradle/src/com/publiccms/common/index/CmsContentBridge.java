package com.publiccms.common.index;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.sanluan.common.base.Base;

@Component
public class CmsContentBridge extends Base implements FieldBridge {
    public static CmsContentAttributeService contentAttributeService;

    @Autowired
    public void init(CmsContentAttributeService contentAttributeService) {
        CmsContentBridge.contentAttributeService = contentAttributeService;
    }

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        CmsContent content = (CmsContent) value;
        CmsContentAttribute entity = contentAttributeService.getEntity(content.getId());
        if (notEmpty(entity)) {
            content.setDescription(content.getDescription() + entity.getText());
        }
    }
}
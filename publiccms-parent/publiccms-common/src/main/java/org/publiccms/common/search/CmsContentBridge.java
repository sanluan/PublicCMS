package org.publiccms.common.search;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.publiccms.entities.cms.CmsContent;
import org.publiccms.entities.cms.CmsContentAttribute;
import org.publiccms.logic.service.cms.CmsContentAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * CmsContentBridge
 * 
 */
@Component
public class CmsContentBridge implements FieldBridge {
    /**
     * 
     */
    public static CmsContentAttributeService contentAttributeService;

    /**
     * @param contentAttributeService
     */
    @Autowired
    public void init(CmsContentAttributeService contentAttributeService) {
        CmsContentBridge.contentAttributeService = contentAttributeService;
    }

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        CmsContent content = (CmsContent) value;
        CmsContentAttribute entity = contentAttributeService.getEntity(content.getId());
        if (null != entity) {
            content.setDescription(content.getDescription() + entity.getText());
        }
    }
}
package com.publiccms.common.search;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.component.BeanComponent;

/**
 *
 * CmsContentBridge
 * 
 */
public class CmsContentBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        CmsContent content = (CmsContent) value;
        CmsContentAttribute entity = BeanComponent.getContentAttributeService().getEntity(content.getId());
        if (null != entity) {
            content.setDescription(content.getDescription() + CommonConstants.BLANK_SPACE + entity.getSearchText());
        }
    }
}
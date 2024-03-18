package com.publiccms.common.search;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.IndexObjectFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.views.pojo.entities.CmsModel;

public class CmsContentAttributeBridge implements TypeBridge<CmsContent> {
    private final IndexFieldReference<String> textField;
    private final IndexFieldReference<String> dictionaryValuesField;
    private final IndexFieldReference<String> filesField;
    private final IndexFieldReference<BigDecimal> minPriceField;
    private final IndexFieldReference<BigDecimal> maxPriceField;
    private final IndexObjectFieldReference extendFieldReference;

    public CmsContentAttributeBridge(IndexFieldReference<String> textField, IndexFieldReference<String> dictionaryValuesField,
            IndexFieldReference<String> filesField, IndexFieldReference<BigDecimal> minPriceField,
            IndexFieldReference<BigDecimal> maxPriceField, IndexObjectFieldReference extendFieldReference) {
        this.textField = textField;
        this.dictionaryValuesField = dictionaryValuesField;
        this.filesField = filesField;
        this.minPriceField = minPriceField;
        this.maxPriceField = maxPriceField;
        this.extendFieldReference = extendFieldReference;
    }

    @Override
    public void write(DocumentElement target, CmsContent bridgedElement, TypeBridgeWriteContext context) {
        CmsContentAttribute attribute = BeanComponent.getContentAttributeService().getEntity(bridgedElement.getId());
        if (null != attribute) {
            if (CommonUtils.notEmpty(attribute.getData())) {
                SysSite site = BeanComponent.getSiteComponent().getSiteById(bridgedElement.getSiteId());
                if (null != site) {
                    CmsModel model = BeanComponent.getModelComponent().getModel(site, bridgedElement.getModelId());
                    if (null != model) {
                        Set<String> searchableFields = model.getSearchableFields();
                        if (CommonUtils.notEmpty(searchableFields)) {
                            Set<String> sortFields = model.getSortableFields();
                            Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                            if (CommonUtils.notEmpty(map)) {
                                DocumentElement extend = target.addObject(extendFieldReference);
                                int i = 1;
                                for (String field : searchableFields) {
                                    if (sortFields.contains(field) && i <= 10) {
                                        extend.addValue(CommonUtils.joinString("sort",i), map.get(field));
                                        i++;
                                    } else {
                                        extend.addValue(field, map.get(field));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            target.addValue(this.textField, attribute.getSearchText());
            target.addValue(this.dictionaryValuesField, attribute.getDictionaryValues());
            target.addValue(this.filesField, attribute.getFilesText());
            target.addValue(this.minPriceField, attribute.getMinPrice());
            target.addValue(this.maxPriceField, attribute.getMaxPrice());
        }
    }
}
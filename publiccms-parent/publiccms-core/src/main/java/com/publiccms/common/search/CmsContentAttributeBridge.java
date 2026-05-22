package com.publiccms.common.search;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.IndexObjectFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentLang;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.views.pojo.entities.CmsModel;

public class CmsContentAttributeBridge implements TypeBridge<CmsContent> {
    private static final String[] SORTABLE_INPUT_TYPES = { Config.INPUTTYPE_NUMBER, Config.INPUTTYPE_BOOLEAN,
            Config.INPUTTYPE_TEXT, Config.INPUTTYPE_DATE, Config.INPUTTYPE_DATETIME, Config.INPUTTYPE_TIME,
            Config.INPUTTYPE_DICTIONARY };

    private final IndexFieldReference<String> textField;
    private final IndexFieldReference<String> dictionaryValuesField;
    private final IndexFieldReference<String> filesField;
    private final IndexFieldReference<BigDecimal> minPriceField;
    private final IndexFieldReference<BigDecimal> maxPriceField;
    private final IndexObjectFieldReference extendFieldReference;
    private final IndexObjectFieldReference langFieldReference;

    public CmsContentAttributeBridge(IndexFieldReference<String> textField, IndexFieldReference<String> dictionaryValuesField,
            IndexFieldReference<String> filesField, IndexFieldReference<BigDecimal> minPriceField,
            IndexFieldReference<BigDecimal> maxPriceField, IndexObjectFieldReference extendFieldReference,
            IndexObjectFieldReference langFieldReference) {
        this.textField = textField;
        this.dictionaryValuesField = dictionaryValuesField;
        this.filesField = filesField;
        this.minPriceField = minPriceField;
        this.maxPriceField = maxPriceField;
        this.extendFieldReference = extendFieldReference;
        this.langFieldReference = langFieldReference;
    }

    @Override
    public void write(DocumentElement target, CmsContent bridgedElement, TypeBridgeWriteContext context) {
        SysSite site = BeanComponent.getSiteComponent().getSiteById(bridgedElement.getSiteId());
        CmsModel model = BeanComponent.getModelComponent().getModel(site, bridgedElement.getModelId());
        if (null != model && null != site) {
            CmsContentAttribute attribute = BeanComponent.getContentAttributeService().getEntity(bridgedElement.getId());
            if (null != attribute) {
                if (CommonUtils.notEmpty(attribute.getData())) {
                    if (CommonUtils.notEmpty(model.getSearchableFields())) {
                        Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                        if (CommonUtils.notEmpty(map)) {
                            DocumentElement extend = target.addObject(extendFieldReference);
                            for (String field : model.getSearchableFields()) {
                                extend.addValue(field, map.get(field));
                            }
                            if (CommonUtils.notEmpty(model.getExtendList())) {
                                for (SysExtendField field : model.getExtendList()) {
                                    if (CommonUtils.notEmpty(field.getSortable())
                                            && ArrayUtils.contains(SORTABLE_INPUT_TYPES, field.getInputType())) {
                                        extend.addValue(field.getSortable(), map.get(field.getId().getCode()));
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
            if (BeanComponent.getSiteAttributeComponent().enableMultilingual(site.getId())) {
                List<CmsContentLang> langList = BeanComponent.getContentLangService().getList(bridgedElement.getId());
                DocumentElement langElement = target.addObject(langFieldReference);
                for (CmsContentLang lang : langList) {
                    langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, "title"),
                            lang.getTitle());
                    langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, "author"),
                            lang.getAuthor());
                    langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, "editor"),
                            lang.getEditor());
                    langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, "description"),
                            lang.getDescription());
                    langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, "text"),
                            lang.getText());
                    if (CommonUtils.notEmpty(model.getSearchableFields())) {
                        Map<String, String> map = ExtendUtils.getExtendMap(lang.getData());
                        if (CommonUtils.notEmpty(map)) {
                            for (String field : model.getSearchableFields()) {
                                langElement.addValue(CommonUtils.joinString(lang.getId().getLang(), Constants.UNDERLINE, field),
                                        map.get(field));
                            }
                        }
                    }
                }
            }
        }
    }
}
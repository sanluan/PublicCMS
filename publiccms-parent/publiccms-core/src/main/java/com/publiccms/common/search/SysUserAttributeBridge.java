package com.publiccms.common.search;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.document.IndexObjectFieldReference;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysUserAttribute;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.BeanComponent;

public class SysUserAttributeBridge implements TypeBridge<SysUser> {
    private final IndexFieldReference<String> textField;
    private final IndexFieldReference<String> dictionaryValuesField;
    private final IndexFieldReference<String> certificationIdsField;
    private final IndexObjectFieldReference extendField;

    public SysUserAttributeBridge(IndexFieldReference<String> textField, IndexFieldReference<String> dictionaryValuesField,
            IndexFieldReference<String> certificationIdsField, IndexObjectFieldReference extendField) {
        this.textField = textField;
        this.dictionaryValuesField = dictionaryValuesField;
        this.certificationIdsField = certificationIdsField;
        this.extendField = extendField;
    }

    @Override
    public void write(DocumentElement target, SysUser bridgedElement, TypeBridgeWriteContext context) {
        SysUserAttribute attribute = BeanComponent.getUserAttributeService().getEntity(bridgedElement.getId());
        if (null != attribute) {
            if (CommonUtils.notEmpty(attribute.getExtendsFields()) && CommonUtils.notEmpty(attribute.getData())) {
                String[] fields = StringUtils.split(attribute.getExtendsFields(), Constants.COMMA);
                Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                if (CommonUtils.notEmpty(fields) && CommonUtils.notEmpty(map)) {
                    DocumentElement extend = target.addObject(extendField);
                    for (String field : fields) {
                        extend.addValue(field, map.get(field));
                    }
                }
            }
            target.addValue(this.textField, attribute.getSearchText());
            target.addValue(this.dictionaryValuesField, attribute.getDictionaryValues());
            target.addValue(this.certificationIdsField, attribute.getCertificationIds());
        }
    }
}
package com.publiccms.common.tools;

import org.springframework.beans.BeanUtils;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentLang;

public class CmsLangUtils {
    private static String ignoreProperties = "id";

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static CmsCategory initCategoryLang(CmsCategory entity, CmsCategoryLang langEntity) {
        if (null != langEntity && null != langEntity.getId().getLang()
                && !langEntity.getId().getLang().equalsIgnoreCase(entity.getLang())) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            entity.setLang(langEntity.getId().getLang());
        }
        return entity;
    }

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static CmsCategoryAttribute initCategoryLang(CmsCategoryAttribute entity, CmsCategoryLang langEntity) {
        if (null != langEntity) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
        }
        return entity;
    }

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static CmsContent initContentLang(CmsContent entity, CmsContentLang langEntity) {
        if (null != langEntity && null != langEntity.getId().getLang()
                && !langEntity.getId().getLang().equalsIgnoreCase(entity.getLang())) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            entity.setLang(langEntity.getId().getLang());
        }
        return entity;
    }

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static CmsContentAttribute initContentLang(CmsContentAttribute entity, CmsContentLang langEntity) {
        if (null != langEntity) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
        }
        return entity;
    }
}

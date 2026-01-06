package com.publiccms.common.tools;

import org.springframework.beans.BeanUtils;

import com.publiccms.common.constants.Constants;
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
     * @param lang
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsCategory entity, String lang, CmsCategoryLang langEntity) {
        if (null != langEntity && null != langEntity.getId().getLang() && !langEntity.getId().getLang().equalsIgnoreCase(lang)) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            entity.setLang(langEntity.getId().getLang());
            return true;
        }
        return false;
    }

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsCategory entity, CmsCategoryLang langEntity) {
        return initLang(entity, entity.getLang(), langEntity);
    }

    /**
     * @param entity
     * @param lang
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsCategoryAttribute entity, CmsCategoryLang langEntity) {
        if (null != langEntity) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            return true;
        }
        return false;
    }

    /**
     * @param entity
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsContent entity, CmsContentLang langEntity) {
        return initLang(entity, entity.getLang(), langEntity);
    }

    /**
     * @param entity
     * @param lang
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsContent entity, String lang, CmsContentLang langEntity) {
        if (null != langEntity && null != langEntity.getId().getLang() && !langEntity.getId().getLang().equalsIgnoreCase(lang)) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            entity.setLang(langEntity.getId().getLang());
            return true;
        }
        return false;
    }

    /**
     * @param entity
     * @param lang
     * @param langEntity
     * @return
     */
    public static boolean initLang(CmsContentAttribute entity, CmsContentLang langEntity) {
        if (null != langEntity) {
            BeanUtils.copyProperties(langEntity, entity, ignoreProperties);
            return true;
        }
        return false;
    }

    public static String getPlaceFilepath(String filepath, String lang, String defaultLang) {
        boolean flag = CommonUtils.empty(lang) || lang.equalsIgnoreCase(defaultLang);
        return CommonUtils.joinString(flag ? null : Constants.SEPARATOR, flag ? null : lang, filepath);
    }

    public static String getFullFilepath(String filepath, String lang, String defaultLang) {
        boolean flag = CommonUtils.empty(lang) || lang.equalsIgnoreCase(defaultLang);
        return CommonUtils.joinString(flag ? null : lang, flag ? null : Constants.SEPARATOR, filepath);
    }
}

package com.publiccms.logic.component.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysLock;
import com.publiccms.entities.sys.SysLockId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.service.sys.SysLockService;

/**
 *
 * LockComponent
 * 
 */
@Component
public class LockComponent implements Config, SiteCache {
    public static final String ITEM_TYPE_LOGIN = "userLogin";
    public static final String ITEM_TYPE_IP_LOGIN = "ipLogin";
    public static final String ITEM_TYPE_REGISTER = "register";
    public static final String ITEM_TYPE_FILEUPLOAD = "fileUpload";
    public static final String ITEM_TYPE_CONTRIBUTE = "contribute";
    public static final String ITEM_TYPE_COMMENT = "comment";
    public static final String[] SYSTEM_ITEM_TYPES = { ITEM_TYPE_LOGIN, ITEM_TYPE_IP_LOGIN, ITEM_TYPE_REGISTER,
            ITEM_TYPE_FILEUPLOAD, ITEM_TYPE_CONTRIBUTE, ITEM_TYPE_COMMENT };
    private static final String[] ITEM_TYPE_LOGINS = new String[] { ITEM_TYPE_LOGIN, ITEM_TYPE_IP_LOGIN };
    /**
     * 
     */
    public static final String CONFIG_CODE = "lock";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;
    /**
    *
    */
    public static final String CONFIG_LOCK_EXPIRY_MINUTES = "expiry_minutes";
    /**
    *
    */
    public static final String CONFIG_LOCK_EXPIRY_LOGIN = "expiry_minutes.login";
    /**
    *
    */
    public static final String CONFIG_LOCK_LOGIN_MAX_COUNT = "login.max_count";
    /**
    *
    */
    public static final String CONFIG_LOCK_IP_LOGIN_MAX_COUNT = "ip.login.max_count";
    /**
    *
    */
    public static final String CONFIG_LOCK_EXPIRY_REGISTER = "expiry_minutes.register";
    /**
    *
    */
    public static final String CONFIG_LOCK_REGISTER_MAX_COUNT = "register.max_count";
    /**
    *
    */
    public static final String CONFIG_LOCK_EXPIRY_FILEUPLOAD = "expiry_minutes.fileupload";
    /**
    *
    */
    public static final String CONFIG_LOCK_FILEUPLOAD_MAX_COUNT = "fileupload.max_count";
    /**
     *
     */
    public static final String CONFIG_LOCK_EXPIRY_CONTRIBUTE = "expiry_minutes.contribute";
    /**
     *
     */
    public static final String CONFIG_LOCK_CONTRIBUTE_MAX_COUNT = "contribute.max_count";
    /**
    *
    */
    public static final String CONFIG_LOCK_EXPIRY_COMMENT = "expiry_minutes.comment";
    /**
    *
    */
    public static final String CONFIG_LOCK_COMMENT_MAX_COUNT = "comment.max_count";
    /**
     * default expiry minutes
     */
    public static final int DEFAULT_EXPIRY_MINUTES = 10;
    /**
     * default operate max count
     */
    public static final int DEFAULT_OPERATE_EXPIRY_MINUTES = 2 * 60;
    /**
     * default operate max count
     */
    public static final int DEFAULT_OPERATE_MAX_COUNT = 3;
    /**
     * default login expiry minutes
     */
    public static final int DEFAULT_LOGIN_EXPIRY_MINUTES = 2 * 60;
    /**
     * default login max count
     */
    public static final int DEFAULT_LOGIN_MAX_COUNT = 5;
    /**
     * default login max count
     */
    public static final int DEFAULT_IP_LOGIN_MAX_COUNT = 20;
    /**
     * default register expiry minutes
     */
    public static final int DEFAULT_REGISTER_EXPIRY_MINUTES = 6 * 60;
    /**
     * default register max count
     */
    public static final int DEFAULT_REGISTER_MAX_COUNT = 3;
    @Resource
    private SysLockService service;

    public int getExpriy(short siteId, String itemType) {
        Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(siteId, CONFIG_CODE);
        int expriy = 0;
        if (ITEM_TYPE_LOGIN.equalsIgnoreCase(itemType) || ITEM_TYPE_IP_LOGIN.equalsIgnoreCase(itemType)) {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_LOGIN), DEFAULT_LOGIN_EXPIRY_MINUTES);
        } else if (ITEM_TYPE_REGISTER.equalsIgnoreCase(itemType)) {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_REGISTER), DEFAULT_REGISTER_EXPIRY_MINUTES);
        } else if (ITEM_TYPE_FILEUPLOAD.equalsIgnoreCase(itemType)) {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_FILEUPLOAD), DEFAULT_OPERATE_MAX_COUNT);
        } else if (ITEM_TYPE_CONTRIBUTE.equalsIgnoreCase(itemType)) {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_CONTRIBUTE), DEFAULT_OPERATE_MAX_COUNT);
        } else if (ITEM_TYPE_COMMENT.equalsIgnoreCase(itemType)) {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_COMMENT), DEFAULT_OPERATE_MAX_COUNT);
        } else {
            expriy = ConfigComponent.getInt(config.get(CONFIG_LOCK_EXPIRY_MINUTES), DEFAULT_EXPIRY_MINUTES);
        }
        return expriy;
    }

    public boolean isLocked(short siteId, String itemType, String itemId, Long userId) {
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(siteId, CONFIG_CODE);
            int expriy = getExpriy(siteId, itemType), maxCount = 0;
            if (ITEM_TYPE_LOGIN.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_LOGIN_MAX_COUNT), DEFAULT_LOGIN_MAX_COUNT);
            } else if (ITEM_TYPE_IP_LOGIN.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_IP_LOGIN_MAX_COUNT), DEFAULT_IP_LOGIN_MAX_COUNT);
            } else if (ITEM_TYPE_REGISTER.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_REGISTER_MAX_COUNT), DEFAULT_REGISTER_MAX_COUNT);
            } else if (ITEM_TYPE_FILEUPLOAD.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_FILEUPLOAD_MAX_COUNT), DEFAULT_OPERATE_MAX_COUNT);
            } else if (ITEM_TYPE_CONTRIBUTE.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_CONTRIBUTE_MAX_COUNT), DEFAULT_OPERATE_MAX_COUNT);
            } else if (ITEM_TYPE_COMMENT.equalsIgnoreCase(itemType)) {
                maxCount = ConfigComponent.getInt(config.get(CONFIG_LOCK_COMMENT_MAX_COUNT), DEFAULT_OPERATE_MAX_COUNT);
            }
            if (expriy > 0) {
                SysLockId id = new SysLockId(siteId, itemType, itemId);
                SysLock entity = service.getEntity(id);
                if (null == entity) {
                    return false;
                } else if (entity.getCreateDate().after(DateUtils.addMinutes(CommonUtils.getDate(), -expriy))) {
                    if (maxCount > 0) {
                        return entity.getCount() >= maxCount;
                    } else if (null == entity.getUserId()) {
                        return true;
                    } else if (entity.getUserId().equals(userId)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public void unLock(short siteId, String itemType, String itemId, Long userId) {
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            SysLockId id = new SysLockId(siteId, itemType, itemId);
            SysLock entity = service.getEntity(id);
            if (null != entity) {
                if (null == entity.getUserId() || entity.getUserId().equals(userId)) {
                    service.delete(id);
                } else {
                    int expriy = getExpriy(siteId, itemType);
                    if (entity.getCreateDate().before(DateUtils.addMinutes(CommonUtils.getDate(), -expriy))) {
                        service.delete(id);
                    }
                }
            }
        }
    }

    public SysLock lock(short siteId, String itemType, String itemId, Long userId, boolean counter) {
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            SysLockId id = new SysLockId(siteId, itemType, itemId);
            SysLock entity = service.getEntity(id);
            if (null == entity) {
                entity = new SysLock(id, 1, new Date());
                entity.setUserId(userId);
                service.save(entity);
            } else {
                int expriy = getExpriy(siteId, itemType);
                if (entity.getCreateDate().before(DateUtils.addMinutes(CommonUtils.getDate(), -expriy))) {
                    entity = service.updateCreateDate(id, 1, userId);
                } else if (null == entity.getUserId() || entity.getUserId().equals(userId)) {
                    if (counter) {
                        entity = service.updateCount(id);
                    }
                }
            }
            return entity;
        }
        return null;
    }

    /**
     * @param siteId
     * @param showAll
     * @return config code or null
     */
    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    /**
     * @param locale
     * @return
     */
    public String getCodeDescription(Locale locale) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_LOCK_EXPIRY_MINUTES, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_MINUTES,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_MINUTES),
                String.valueOf(DEFAULT_EXPIRY_MINUTES)));
        extendFieldList.add(new SysExtendField(CONFIG_LOCK_EXPIRY_LOGIN, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_LOGIN,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_LOGIN),
                String.valueOf(DEFAULT_LOGIN_EXPIRY_MINUTES)));
        extendFieldList.add(new SysExtendField(CONFIG_LOCK_LOGIN_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_LOGIN_MAX_COUNT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_LOGIN_MAX_COUNT),
                String.valueOf(DEFAULT_LOGIN_MAX_COUNT)));
        extendFieldList
                .add(new SysExtendField(CONFIG_LOCK_IP_LOGIN_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_IP_LOGIN_MAX_COUNT,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_IP_LOGIN_MAX_COUNT),
                        String.valueOf(DEFAULT_IP_LOGIN_MAX_COUNT)));
        extendFieldList.add(new SysExtendField(CONFIG_LOCK_EXPIRY_REGISTER, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_REGISTER,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_REGISTER),
                String.valueOf(DEFAULT_REGISTER_EXPIRY_MINUTES)));
        extendFieldList
                .add(new SysExtendField(CONFIG_LOCK_REGISTER_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_REGISTER_MAX_COUNT,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_REGISTER_MAX_COUNT),
                        String.valueOf(DEFAULT_REGISTER_MAX_COUNT)));
        extendFieldList
                .add(new SysExtendField(CONFIG_LOCK_EXPIRY_FILEUPLOAD, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_FILEUPLOAD,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_FILEUPLOAD),
                        String.valueOf(DEFAULT_OPERATE_EXPIRY_MINUTES)));
        extendFieldList.add(
                new SysExtendField(CONFIG_LOCK_FILEUPLOAD_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_FILEUPLOAD_MAX_COUNT,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_FILEUPLOAD_MAX_COUNT),
                        String.valueOf(DEFAULT_OPERATE_MAX_COUNT)));
        extendFieldList
                .add(new SysExtendField(CONFIG_LOCK_EXPIRY_CONTRIBUTE, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_CONTRIBUTE,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_CONTRIBUTE),
                        String.valueOf(DEFAULT_OPERATE_EXPIRY_MINUTES)));
        extendFieldList.add(
                new SysExtendField(CONFIG_LOCK_CONTRIBUTE_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_CONTRIBUTE_MAX_COUNT,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_CONTRIBUTE_MAX_COUNT),
                        String.valueOf(DEFAULT_OPERATE_MAX_COUNT)));
        extendFieldList.add(new SysExtendField(CONFIG_LOCK_EXPIRY_COMMENT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_EXPIRY_COMMENT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_EXPIRY_COMMENT),
                String.valueOf(DEFAULT_OPERATE_EXPIRY_MINUTES)));
        extendFieldList
                .add(new SysExtendField(CONFIG_LOCK_COMMENT_MAX_COUNT, INPUTTYPE_NUMBER, false, CONFIG_LOCK_COMMENT_MAX_COUNT,
                        getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_LOCK_COMMENT_MAX_COUNT),
                        String.valueOf(DEFAULT_OPERATE_MAX_COUNT)));
        return extendFieldList;
    }

    @Override
    public void clear() {
        Date now = CommonUtils.getDate();
        List<Short> list1 = service.getSiteIdListByItemTypes(ITEM_TYPE_LOGINS, null);
        if (CommonUtils.notEmpty(list1)) {
            for (Short siteId : list1) {
                int expriy = getExpriy(siteId, ITEM_TYPE_LOGIN);
                service.deleteByItemTypes(ITEM_TYPE_LOGINS, null, DateUtils.addMinutes(now, -expriy));
            }
        }
        List<Short> list2 = service.getSiteIdList(ITEM_TYPE_REGISTER, null);
        if (CommonUtils.notEmpty(list2)) {
            for (Short siteId : list2) {
                int expriy = getExpriy(siteId, ITEM_TYPE_REGISTER);
                service.delete(ITEM_TYPE_REGISTER, null, DateUtils.addMinutes(now, -expriy));
            }
        }
        List<Short> list3 = service.getSiteIdList(null, SYSTEM_ITEM_TYPES);
        if (CommonUtils.notEmpty(list3)) {
            for (Short siteId : list3) {
                int expriy = getExpriy(siteId, null);
                service.delete(null, SYSTEM_ITEM_TYPES, DateUtils.addMinutes(now, -expriy));
            }
        }
        List<Short> list4 = service.getSiteIdList(ITEM_TYPE_FILEUPLOAD, null);
        if (CommonUtils.notEmpty(list4)) {
            for (Short siteId : list4) {
                int expriy = getExpriy(siteId, ITEM_TYPE_FILEUPLOAD);
                service.delete(ITEM_TYPE_FILEUPLOAD, null, DateUtils.addMinutes(now, -expriy));
            }
        }
        List<Short> list5 = service.getSiteIdList(ITEM_TYPE_CONTRIBUTE, null);
        if (CommonUtils.notEmpty(list5)) {
            for (Short siteId : list5) {
                int expriy = getExpriy(siteId, ITEM_TYPE_CONTRIBUTE);
                service.delete(ITEM_TYPE_CONTRIBUTE, null, DateUtils.addMinutes(now, -expriy));
            }
        }
        List<Short> list6 = service.getSiteIdList(ITEM_TYPE_COMMENT, null);
        if (CommonUtils.notEmpty(list6)) {
            for (Short siteId : list6) {
                int expriy = getExpriy(siteId, ITEM_TYPE_COMMENT);
                service.delete(ITEM_TYPE_COMMENT, null, DateUtils.addMinutes(now, -expriy));
            }
        }
    }

    @Override
    public void clear(short siteId) {
        Date now = CommonUtils.getDate();
        int expriy = getExpriy(siteId, ITEM_TYPE_LOGIN);
        service.deleteByItemTypes(ITEM_TYPE_LOGINS, null, DateUtils.addMinutes(now, -expriy));
        expriy = getExpriy(siteId, ITEM_TYPE_REGISTER);
        service.delete(ITEM_TYPE_REGISTER, null, DateUtils.addMinutes(now, -expriy));
        expriy = getExpriy(siteId, ITEM_TYPE_FILEUPLOAD);
        service.delete(ITEM_TYPE_FILEUPLOAD, null, DateUtils.addMinutes(now, -expriy));
        expriy = getExpriy(siteId, ITEM_TYPE_CONTRIBUTE);
        service.delete(ITEM_TYPE_CONTRIBUTE, null, DateUtils.addMinutes(now, -expriy));
        expriy = getExpriy(siteId, ITEM_TYPE_COMMENT);
        service.delete(ITEM_TYPE_COMMENT, null, DateUtils.addMinutes(now, -expriy));
        expriy = getExpriy(siteId, null);
        service.delete(null, SYSTEM_ITEM_TYPES, DateUtils.addMinutes(now, -expriy));
    }
}
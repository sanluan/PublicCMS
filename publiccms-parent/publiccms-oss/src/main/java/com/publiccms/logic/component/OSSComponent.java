package com.publiccms.logic.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;

/**
 * OSSComponent 对象存储组件
 * 
 */
@Component
public class OSSComponent implements Config {
    private String adminContextPath;
    /**
     * 
     */
    public static final String CONFIG_CODE = "oss";

    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);

    public static final String CONFIG_ACCESSKEYID = "accessKeyId";
    public static final String CONFIG_ACCESSKEYSECRET = "accessKeySecret";
    public static final String CONFIG_BUCKET = "bucket";
    public static final String CONFIG_BUCKET_URL = "bucketUrl";
    public static final String CONFIG_REGION = "region";
    public static final String CONFIG_ENDPOINT = "endpoint";
    public static final String CONFIG_PRIVATE_BUCKET = "privateBucket";
    public static final String CONFIG_PRIVATE_BUCKET_URL = "privateBucketUrl";
    public static final String CONFIG_PRIVATE_REGION = "privateRegion";
    public static final String CONFIG_PRIVATE_ENDPOINT = "privateEndpoint";

    /**
     * @param adminContextPath
     *            the adminContextPath to set
     */
    public void setAdminContextPath(String adminContextPath) {
        this.adminContextPath = adminContextPath;
    }

    /**
     * @return the adminContextPath
     */
    public String getAdminContextPath() {
        return adminContextPath;
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
    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_ACCESSKEYID, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ACCESSKEYID)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ACCESSKEYID,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_ACCESSKEYSECRET, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ACCESSKEYSECRET)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ACCESSKEYSECRET,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_REGION, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_REGION)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_REGION,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_BUCKET, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_BUCKET)), null));
        extendFieldList.add(new SysExtendField(CONFIG_BUCKET_URL, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_BUCKET_URL)), null));
        extendFieldList.add(new SysExtendField(CONFIG_ENDPOINT, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_ENDPOINT)), null));
        
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATE_REGION, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATE_REGION)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_REGION,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATE_BUCKET, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATE_BUCKET)), null));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATE_BUCKET_URL, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATE_BUCKET_URL)),
                null));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATE_ENDPOINT, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATE_ENDPOINT)),
                null));
        return extendFieldList;
    }

    @Override
    public boolean exportable() {
        return false;
    }

}

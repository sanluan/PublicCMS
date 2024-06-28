package com.publiccms.common.constants;

import java.util.UUID;

import com.publiccms.common.copyright.CmsCopyright;
import com.publiccms.common.copyright.Copyright;
import com.publiccms.common.copyright.License;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsVersion
 *
 */
public class CmsVersion {
    private CmsVersion() {
    }

    private static final String clusterId = UUID.randomUUID().toString();
    private static boolean master = false;
    private static boolean initialized = false;
    private static boolean scheduled = true;
    private static Copyright copyright = new CmsCopyright();

    /**
     * base version
     */
    public static final String BASE_VERSION = "V4.0";

    /**
     * @return version
     */
    public static final String getVersion() {
        return BASE_VERSION + ".202406";
    }

    /**
     * @return revision
     */
    public static final String getRevision() {
        return "b";
    }

    /**
     * @return whether the authorization edition
     */
    public static boolean isAuthorizationEdition() {
        return copyright.verify(getLicense());
    }

    /**
     * @param domain
     * @return whether the domain authorized
     */
    public static boolean verifyDomain(String domain) {
        return copyright.verify(getLicense(), domain);
    }

    /**
     * @return license
     */
    public static License getLicense() {
        return copyright.getLicense(CommonUtils.joinString(CommonConstants.CMS_FILEPATH, CommonConstants.LICENSE_FILENAME));
    }

    /**
     * @return cluster id
     */
    public static final String getClusterId() {
        return clusterId;
    }

    /**
     * @return whether the master node
     */
    public static boolean isMaster() {
        return master;
    }

    /**
     * @param master
     */
    public static void setMaster(boolean master) {
        CmsVersion.master = master;
    }

    /**
     * @return whether initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized
     */
    public static void setInitialized(boolean initialized) {
        CmsVersion.initialized = initialized;
    }

    /**
     * @return the scheduled
     */
    public static boolean isScheduled() {
        return scheduled && initialized;
    }

    /**
     * @param scheduled
     *            the scheduled to set
     */
    public static void setScheduled(boolean scheduled) {
        CmsVersion.scheduled = scheduled;
    }
}
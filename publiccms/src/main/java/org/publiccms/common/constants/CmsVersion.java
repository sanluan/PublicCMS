package org.publiccms.common.constants;

import java.util.UUID;

/**
 *
 * CmsVersion
 *
 */
public class CmsVersion {
    private static final String clusterId = UUID.randomUUID().toString();
    private static boolean master = false;
    private static boolean initialized = false;

    /**
     * @return
     */
    public static final String getVersion() {
        return "V2017.0520";
    }

    /**
     * @return
     */
    public static boolean isPreview() {
        return false;
    }

    /**
     * @return
     */
    public static final String getClusterId() {
        return clusterId;
    }

    /**
     * @return
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
     * @return
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
}
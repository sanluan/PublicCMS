package com.publiccms.common.constants;

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
     * @return version
     */
    public static final String getVersion() {
        return "V2021";
    }
    /**
     * @return revision
     */
    public static final String getRevision() {
        return "a";
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
}
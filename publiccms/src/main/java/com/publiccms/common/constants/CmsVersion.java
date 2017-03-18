package com.publiccms.common.constants;

import java.util.UUID;

public class CmsVersion {
    private static final String clusterId = UUID.randomUUID().toString();
    private static boolean master = false;
    private static boolean initialized = false;

    public static final String getVersion() {
        return "V2017.0318";
    }

    public static boolean isPreview() {
        return false;
    }

    public static final String getClusterId() {
        return clusterId;
    }

    public static boolean isMaster() {
        return master;
    }

    public static void setMaster(boolean master) {
        CmsVersion.master = master;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized(boolean initialized) {
        CmsVersion.initialized = initialized;
    }
}
package com.publiccms.common.constants;

import java.util.UUID;

public class CmsVersion {
    private static final String clusterId = UUID.randomUUID().toString();
    private static boolean master = false;

    public static final String getVersion() {
        return "V2016.0828";
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
}
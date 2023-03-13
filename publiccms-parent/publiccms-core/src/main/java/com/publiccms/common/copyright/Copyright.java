package com.publiccms.common.copyright;

/**
 *
 * Copyright
 *
 */
public interface Copyright {

    /**
     * @param license
     * @return whether to pass verify
     */
    boolean verify(License license);

    /**
     * @param licenseFilePath
     * @return license
     */
    License getLicense(String licenseFilePath);

    /**
     * @param license
     * @param domain
     * @return whether to pass verify
     */
    boolean verify(License license, String domain);
}

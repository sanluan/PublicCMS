package com.publiccms.common.copyright;

/**
 *
 * Copyright
 *
 */
public interface Copyright {

    /**
     * @param licenseFilePath
     * @return whether to pass verify
     */
    boolean verify(String licenseFilePath);

    /**
     * @param licenseFilePath
     * @return license
     */
    License getLicense(String licenseFilePath);

    /**
     * @param licenseFilePath
     * @param domain
     * @return whether to pass verify
     */
    boolean verify(String licenseFilePath, String domain);
}

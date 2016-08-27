package com.publiccms.common.spi;

public interface Pluginable {
    /**
     * 
     */
    public String getCode();

    /**
     * 
     */
    public boolean supportWidget();

    /**
     * @param arg
     * @return
     */
    public String dealWidget(String... args);
}

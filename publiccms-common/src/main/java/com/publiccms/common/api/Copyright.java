package com.publiccms.common.api;

public interface Copyright {
    public boolean verify(String dataFilePath);

    public boolean activate(String activateCode);
}

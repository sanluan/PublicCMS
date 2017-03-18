package com.publiccms.controller.web.changyan.pojo;

import java.io.Serializable;

public class LogoutResult implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int code;

    public LogoutResult(int code, int reload_page) {
        this.code = code;
        this.reload_page = reload_page;
    }

    public int reload_page;

    public int getReload_page() {
        return reload_page;
    }

    public void setReload_page(int reload_page) {
        this.reload_page = reload_page;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

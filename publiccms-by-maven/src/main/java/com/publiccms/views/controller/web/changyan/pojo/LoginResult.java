package com.publiccms.views.controller.web.changyan.pojo;

import java.io.Serializable;

public class LoginResult implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int user_id;
    private int reload_page = 0;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReload_page() {
        return reload_page;
    }

    public void setReload_page(int reload_page) {
        this.reload_page = reload_page;
    }
}

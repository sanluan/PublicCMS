package com.publiccms.controller.web.changyan.pojo;

import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int is_login;
    private User user;

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

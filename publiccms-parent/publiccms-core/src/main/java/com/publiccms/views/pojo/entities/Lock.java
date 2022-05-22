package com.publiccms.views.pojo.entities;

import com.publiccms.entities.sys.SysLock;
import com.publiccms.entities.sys.SysUser;

public class Lock extends SysLock {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Lock(SysLock lock, SysUser user) {
        super(lock.getId(), lock.getUserId(), lock.getCount(), lock.getCreateDate());
        if (null != user) {
            setNickname(user.getNickname());
        }
    }

    private String nickname;

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     *            the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}

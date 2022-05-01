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
            setNickName(user.getNickName());
        }
    }

    private String nickName;

    /**
     * @return the nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName
     *            the nickName to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}

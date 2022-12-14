package com.publiccms.common.handler;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * UsernamePassword proxy authenticator
 * <p>
 * 用户名密码代理认证
 * 
 */
public class UsernamePasswordAuthenticator extends Authenticator {

    private PasswordAuthentication auth;

    /**
     * @param username
     * @param password
     */
    public UsernamePasswordAuthenticator(String username, String password) {
        auth = new PasswordAuthentication(username, password.toCharArray());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return auth;
    }
}

package com.publiccms.common.proxy;

import java.net.PasswordAuthentication;

/**
 *
 * UsernamePasswordAuthenticator
 * 
 */
public class UsernamePasswordAuthenticator extends java.net.Authenticator {
    
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

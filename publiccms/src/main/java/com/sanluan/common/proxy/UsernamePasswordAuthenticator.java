package com.sanluan.common.proxy;

import java.net.PasswordAuthentication;

public class UsernamePasswordAuthenticator extends java.net.Authenticator {
    private PasswordAuthentication auth;

    public UsernamePasswordAuthenticator(String username, String password) {
        auth = new PasswordAuthentication(username, password.toCharArray());
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return auth;
    }
}

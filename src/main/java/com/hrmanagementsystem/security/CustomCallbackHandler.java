package com.hrmanagementsystem.security;

import javax.security.auth.callback.*;
import java.io.IOException;

public class CustomCallbackHandler implements CallbackHandler {
    private final String username;
    private final String password;

    public CustomCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback nameCallback) {
                nameCallback.setName(username);
            } else if (callback instanceof PasswordCallback passwordCallback) {
                passwordCallback.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Unsupported callback type");
            }
        }
    }
}
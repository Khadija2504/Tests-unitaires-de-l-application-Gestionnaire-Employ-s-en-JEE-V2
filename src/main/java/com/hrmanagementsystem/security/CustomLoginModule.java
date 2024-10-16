package com.hrmanagementsystem.security;

import com.hrmanagementsystem.dao.implementations.UserDAO;
import com.hrmanagementsystem.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

public class CustomLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;

    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    private String username;
    private char[] password;

    private UserPrincipal userPrincipal;
    private RolePrincipal rolePrincipal;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            if (tmpPassword == null) {
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            ((PasswordCallback) callbacks[1]).clearPassword();
        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException("Error: " + uce.getCallback().toString() + " not available");
        }

        boolean usernameCorrect = false;
        boolean passwordCorrect = false;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.findByEmail(username);
            if (user != null) {
                usernameCorrect = true;
                if (user.authenticate(username, new String(password))) {
                    passwordCorrect = true;
                    userPrincipal = new UserPrincipal(username);
                    rolePrincipal = new RolePrincipal(user.getRole().name());
                }
            }
        } finally {
            em.close();
            emf.close();
        }

        if (usernameCorrect && passwordCorrect) {
            succeeded = true;
            return true;
        } else {
            succeeded = false;
            username = null;
            password = null;
            if (!usernameCorrect) {
                throw new LoginException("User does not exist");
            } else {
                throw new LoginException("Invalid password");
            }
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (!succeeded) {
            return false;
        } else {
            subject.getPrincipals().add(userPrincipal);
            subject.getPrincipals().add(rolePrincipal);
            username = null;
            password = null;
            commitSucceeded = true;
            return true;
        }
    }

    @Override
    public boolean abort() throws LoginException {
        if (!succeeded) {
            return false;
        } else if (succeeded && !commitSucceeded) {
            succeeded = false;
            username = null;
            password = null;
            userPrincipal = null;
            rolePrincipal = null;
        } else {
            logout();
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        subject.getPrincipals().remove(rolePrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        password = null;
        userPrincipal = null;
        rolePrincipal = null;
        return true;
    }
}

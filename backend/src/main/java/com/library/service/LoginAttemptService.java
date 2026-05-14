package com.library.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_MINUTES = 30;

    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    private static class LoginAttempt {
        int attempts;
        long lockUntil;

        LoginAttempt(int attempts, long lockUntil) {
            this.attempts = attempts;
            this.lockUntil = lockUntil;
        }
    }

    public void loginFailed(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            attempt = new LoginAttempt(1, 0);
        } else {
            attempt.attempts++;
            if (attempt.attempts >= MAX_ATTEMPTS) {
                attempt.lockUntil = System.currentTimeMillis() + (LOCK_TIME_MINUTES * 60 * 1000);
            }
        }
        loginAttempts.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        loginAttempts.remove(username);
    }

    public boolean isLocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return false;
        }
        if (attempt.lockUntil > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public int getRemainingAttempts(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return MAX_ATTEMPTS;
        }
        if (attempt.lockUntil > System.currentTimeMillis()) {
            return 0;
        }
        return MAX_ATTEMPTS - attempt.attempts;
    }

    public long getLockRemainingSeconds(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null || attempt.lockUntil <= System.currentTimeMillis()) {
            return 0;
        }
        return (attempt.lockUntil - System.currentTimeMillis()) / 1000;
    }
}

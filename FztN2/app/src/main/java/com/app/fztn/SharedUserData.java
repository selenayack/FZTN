package com.app.fztn;

public class SharedUserData {
    private static SharedUserData instance;

    private long userId;
    private String dogumTarihi; // Diğer alanlar...

    private SharedUserData() {
        // Private constructor to prevent instantiation
    }

    public static synchronized SharedUserData getInstance() {
        if (instance == null) {
            instance = new SharedUserData();
        }
        return instance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

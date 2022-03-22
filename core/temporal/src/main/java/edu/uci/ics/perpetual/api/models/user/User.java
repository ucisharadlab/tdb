package edu.uci.ics.perpetual.api.models.user;

public class User {
    private int id;
    private int authId;
    private String name;
    private String email;
    private boolean admin;

    public User(int id, int authId, String name, String email, boolean admin) {
        this.id = id;
        this.authId = authId;
        this.name = name;
        this.email = email;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

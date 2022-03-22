package edu.uci.ics.perpetual.api.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostUser {
    private int authId;
    private String name;
    private String email;
    private boolean admin;

    public PostUser (@JsonProperty(value="authId", required=true) int authId,
                     @JsonProperty(value="name", required=true)   String name,
                     @JsonProperty(value="email", required=true)  String email,
                     @JsonProperty(value="admin", required=true) boolean admin) {
        this.authId = authId;
        this.name = name;
        this.email = email;
        this.admin = admin;
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

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

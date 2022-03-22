package edu.uci.ics.perpetual.api.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = "dataValid")
public class PostEntity {
    private String name;
    private int entityTypeId;
    private Map<String, Object> payload;

    public PostEntity (@JsonProperty(value="name", required=true)         String name,
                       @JsonProperty(value="entityTypeId", required=true) int entityTypeId,
                       @JsonProperty(value="payload")                     Map<String, Object> payload) {
        this.name = name;
        this.entityTypeId = entityTypeId;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(int entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}

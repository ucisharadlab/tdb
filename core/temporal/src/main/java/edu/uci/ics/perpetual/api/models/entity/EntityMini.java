package edu.uci.ics.perpetual.api.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityMini {
    private int id;
    private String name;
    private int entityClassId;
    private String entityClassName;
    private int entityTypeId;
    private String entityTypeName;
    private Map<String, Object> payload;

    public EntityMini(int id, String name, int entityClassId, String entityClassName,
                      int entityTypeId, String entityTypeName, Map<String, Object> payload) {
        this.id = id;
        this.name = name;
        this.entityClassId = entityClassId;
        this.entityClassName = entityClassName;
        this.entityTypeId = entityTypeId;
        this.entityTypeName = entityTypeName;
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntityClassId() {
        return entityClassId;
    }

    public void setEntityClassId(int entityClassId) {
        this.entityClassId = entityClassId;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public int getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(int entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}

package edu.uci.ics.perpetual.api.models.entityType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
public class EntityTypeMini {
    private int id;
    private String name;
    private Integer supertypeId;
    private String supertypeName;
    private List<EntityTypeProperty> properties;

    public EntityTypeMini(int id, String name, Integer supertypeId, String supertypeName, List<EntityTypeProperty> properties) {
        this.id = id;
        this.name = name;
        this.supertypeId = supertypeId;
        this.supertypeName = supertypeName;
        this.properties = properties;
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

    public Integer getSupertypeId() {
        return supertypeId;
    }

    public void setSupertypeId(Integer supertypeId) {
        this.supertypeId = supertypeId;
    }

    public String getSupertypeName() {
        return supertypeName;
    }

    public void setSupertypeName(String supertypeName) {
        this.supertypeName = supertypeName;
    }

    public List<EntityTypeProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<EntityTypeProperty> properties) {
        this.properties = properties;
    }
}

package edu.uci.ics.perpetual.api.models.entityType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityType {
    private int id;
    private String name;
    private List<EntityTypeBasic> subtypes;
    private List<EntityTypeBasic> supertypes;
    private List<EntityTypeProperty> properties;

    public EntityType(int id, String name, List<EntityTypeBasic> subtypes,
                      List<EntityTypeBasic> supertypes, List<EntityTypeProperty> properties) {
        this.id = id;
        this.name = name;
        this.subtypes = subtypes;
        this.supertypes = supertypes;
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

    public List<EntityTypeBasic> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<EntityTypeBasic> subtypes) {
        this.subtypes = subtypes;
    }

    public List<EntityTypeBasic> getSupertypes() {
        return supertypes;
    }

    public void setSupertypes(List<EntityTypeBasic> supertypes) {
        this.supertypes = supertypes;
    }

    public List<EntityTypeProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<EntityTypeProperty> properties) {
        this.properties = properties;
    }
}

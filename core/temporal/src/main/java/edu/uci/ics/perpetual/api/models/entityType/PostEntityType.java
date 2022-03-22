package edu.uci.ics.perpetual.api.models.entityType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
public class PostEntityType {
    private String name;
    private int supertypeId;
    private List<Integer> properties;

    public PostEntityType (@JsonProperty(value="name", required=true) String name,
                           @JsonProperty(value="supertypeId", required=true) int supertypeId,
                           @JsonProperty(value="properties") List<Integer> properties) {
        this.name = name;
        this.supertypeId = supertypeId;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSupertypeId() {
        return supertypeId;
    }

    public void setSupertypeId(int supertypeId) {
        this.supertypeId = supertypeId;
    }

    public List<Integer> getProperties() {
        return properties;
    }

    public void setProperties(List<Integer> properties) {
        this.properties = properties;
    }
}

package edu.uci.ics.perpetual.api.models.entityType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
public class PutEntityType {
    private String name;
    private List<Integer> properties;

    public PutEntityType (@JsonProperty(value="name", required=true) String name,
                          @JsonProperty(value="properties") List<Integer> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<Integer> getProperties () {
        return properties;
    }

    public void setProperties (List<Integer> properties) {
        this.properties = properties;
    }
}

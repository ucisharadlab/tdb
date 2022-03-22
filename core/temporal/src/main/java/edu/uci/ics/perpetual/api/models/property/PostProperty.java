package edu.uci.ics.perpetual.api.models.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PostProperty {
    private String name;
    private int propertyClassId;
    private Integer obsTypeId;
    private Integer actTypeId;
    private String value;

    public PostProperty (@JsonProperty(value="name", required=true) String name,
                         @JsonProperty(value="propertyClassId", required=true) int propertyClassId,
                         @JsonProperty(value="obsTypeId") Integer obsTypeId,
                         @JsonProperty(value="actTypeId") Integer actTypeId,
                         @JsonProperty(value="value") String value) {
        this.name = name;
        this.propertyClassId = propertyClassId;
        this.obsTypeId = obsTypeId;
        this.actTypeId = actTypeId;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPropertyClassId() {
        return propertyClassId;
    }

    public void setPropertyClassId(int propertyClassId) {
        this.propertyClassId = propertyClassId;
    }

    public Integer getObsTypeId() {
        return obsTypeId;
    }

    public void setObsTypeId(Integer obsTypeId) {
        this.obsTypeId = obsTypeId;
    }

    public Integer getActTypeId() {
        return actTypeId;
    }

    public void setActTypeId(Integer actTypeId) {
        this.actTypeId = actTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

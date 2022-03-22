package edu.uci.ics.perpetual.api.models.property;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PutProperty {
    private String name;
    private Integer obsTypeId;
    private Integer actTypeId;
    private String value;

    public PutProperty (@JsonProperty(value="name", required=true) String name,
                         @JsonProperty(value="obsTypeId") Integer obsTypeId,
                         @JsonProperty(value="actTypeId") Integer actTypeId,
                         @JsonProperty(value="value") String value) {
        this.name = name;
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

package edu.uci.ics.perpetual.api.models.deviceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PutDeviceType {
    private String name;

    public PutDeviceType (@JsonProperty(value="name", required=true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

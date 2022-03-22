package edu.uci.ics.perpetual.api.models.deviceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PostDeviceType {
    private String name;
    private int deviceClassId;

    public PostDeviceType(@JsonProperty(value="name", required=true) String name,
                          @JsonProperty(value="deviceClassId", required=true) int deviceClassId) {
        this.name = name;
        this.deviceClassId = deviceClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeviceClassId() {
        return deviceClassId;
    }

    public void setDeviceClassId(int deviceClassId) {
        this.deviceClassId = deviceClassId;
    }
}

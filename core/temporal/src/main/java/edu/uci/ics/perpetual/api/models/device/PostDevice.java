package edu.uci.ics.perpetual.api.models.device;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PostDevice {
    private String name;
    private int deviceTypeId;

    public PostDevice (@JsonProperty(value="name", required=true) String name,
                       @JsonProperty(value="deviceTypeId", required=true) int deviceTypeId) {
        this.name = name;
        this.deviceTypeId = deviceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
}

package edu.uci.ics.perpetual.api.models.deviceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceType {
    private int id;
    private String name;
    private int deviceClassId;
    private String deviceClassName;

    public DeviceType(int id, String name, int deviceClassId, String deviceClassName) {
        this.id = id;
        this.name = name;
        this.deviceClassId = deviceClassId;
        this.deviceClassName = deviceClassName;
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

    public int getDeviceClassId() {
        return deviceClassId;
    }

    public void setDeviceClassId(int deviceClassId) {
        this.deviceClassId = deviceClassId;
    }

    public String getDeviceClassName() {
        return deviceClassName;
    }

    public void setDeviceClassName(String deviceClassName) {
        this.deviceClassName = deviceClassName;
    }
}

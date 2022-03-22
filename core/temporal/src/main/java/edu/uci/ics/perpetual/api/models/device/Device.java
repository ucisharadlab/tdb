package edu.uci.ics.perpetual.api.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {
    private int id;
    private String name;
    private int deviceClassId;
    private String deviceClassName;
    private int deviceTypeId;
    private String deviceTypeName;

    public Device(int id, String name, int deviceClassId, String deviceClassName, int deviceTypeId, String deviceTypeName) {
        this.id = id;
        this.name = name;
        this.deviceClassId = deviceClassId;
        this.deviceClassName = deviceClassName;
        this.deviceTypeId = deviceTypeId;
        this.deviceTypeName = deviceTypeName;
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

    public int getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }
}

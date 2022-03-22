package edu.uci.ics.perpetual.api.models.observation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Map;

@JsonIgnoreProperties(value = "dataValid")
public class PostObs {
    private Timestamp timestamp;
    private Integer deviceId;
    private Map<String, Object> payload;

    public PostObs(@JsonProperty(value="timestamp")               Timestamp timestamp,
                   @JsonProperty(value="deviceId")                Integer deviceId,
                   @JsonProperty(value="payload", required=true)  Map<String, Object> payload) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.payload = payload;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId (Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}

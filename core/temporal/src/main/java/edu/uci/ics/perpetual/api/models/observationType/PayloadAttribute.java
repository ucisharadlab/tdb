package edu.uci.ics.perpetual.api.models.observationType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PayloadAttribute {
    private String name;
    private String type;

    public PayloadAttribute(@JsonProperty(value="name", required=true)  String name,
                            @JsonProperty(value="type", required=true)  String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

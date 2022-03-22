package edu.uci.ics.perpetual.api.models.observationType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(value = "dataValid")
public class PostObsType {
    private String name;
    private List<PayloadAttribute> payload;
    private boolean isSemantic;

    public PostObsType (@JsonProperty(value="name", required=true)     String name,
                        @JsonProperty(value="payload", required=true)  List<PayloadAttribute> payload,
                        @JsonProperty(value="isSemantic")              Boolean isSemantic) {
        this.name = name;
        this.payload = payload;
        this.isSemantic = isSemantic==null ? false : isSemantic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PayloadAttribute> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadAttribute> payload) {
        this.payload = payload;
    }

    public boolean getIsSemantic() {
        return isSemantic;
    }

    public void setIsSemantic(boolean semantic) {
        isSemantic = semantic;
    }
}

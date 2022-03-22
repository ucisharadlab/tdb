package edu.uci.ics.perpetual.api.models.observationType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObsType {

    private int id;
    private String name;
    private List<Map<String, String>> payload;
    private boolean isSemantic;

    public ObsType(int id, String name, List<Map<String, String>> payload, boolean isSemantic) {
        this.id = id;
        this.name = name;
        this.payload = payload;
        this.isSemantic = isSemantic;
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

    public List<Map<String, String>> getPayload() {
        return payload;
    }

    public void setPayload(List<Map<String, String>> payload) {
        this.payload = payload;
    }

    public boolean isSemantic() {
        return isSemantic;
    }

    public void setSemantic(boolean semantic) {
        isSemantic = semantic;
    }
}

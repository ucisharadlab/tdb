package edu.uci.ics.perpetual.api.models.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {
    private int id;
    private String name;
    private int propertyClassId;
    private String propertyClassName;
    private Integer obsTypeId;
    private String obsTypeName;
    private Integer actTypeId;
    private String actTypeName;
    private String value;

    public Property(int id, String name, int propertyClassId, String propertyClassName,
                    Integer obsTypeId, String obsTypeName, Integer actTypeId, String actTypeName, String value) {
        this.id = id;
        this.name = name;
        this.propertyClassId = propertyClassId;
        this.propertyClassName = propertyClassName;
        this.obsTypeId = obsTypeId;
        this.obsTypeName = obsTypeName;
        this.actTypeId = actTypeId;
        this.actTypeName = actTypeName;
        this.value = value;
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

    public int getPropertyClassId() {
        return propertyClassId;
    }

    public void setPropertyClassId(int propertyClassId) {
        this.propertyClassId = propertyClassId;
    }

    public String getPropertyClassName() {
        return propertyClassName;
    }

    public void setPropertyClassName(String propertyClassName) {
        this.propertyClassName = propertyClassName;
    }

    public Integer getObsTypeId() {
        return obsTypeId;
    }

    public void setObsTypeId(Integer obsTypeId) {
        this.obsTypeId = obsTypeId;
    }

    public String getObsTypeName() {
        return obsTypeName;
    }

    public void setObsTypeName(String obsTypeName) {
        this.obsTypeName = obsTypeName;
    }

    public Integer getActTypeId() {
        return actTypeId;
    }

    public void setActTypeId(Integer actTypeId) {
        this.actTypeId = actTypeId;
    }

    public String getActTypeName() {
        return actTypeName;
    }

    public void setActTypeName(String actTypeName) {
        this.actTypeName = actTypeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

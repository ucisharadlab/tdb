package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.entities.EntityType;

public class TMap {

    private EntityType eType;

    private Property property;

    public TMap() {

    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public EntityType geteType() {
        return eType;
    }

    public void seteType(EntityType eType) {
        this.eType = eType;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}

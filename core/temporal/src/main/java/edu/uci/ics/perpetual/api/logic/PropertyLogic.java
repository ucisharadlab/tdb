package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.property.PostProperty;
import edu.uci.ics.perpetual.api.models.property.Property;
import edu.uci.ics.perpetual.api.models.property.PropertyClass;
import edu.uci.ics.perpetual.api.models.property.PutProperty;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PropertyLogic {

    public static Response get (Connection con) {

        String query =
            "SELECT property.id, property.name AS \"name\", property_class.id AS \"propertyClassId\", " +
            "    property_class.name AS \"propertyClassName\", observation_type.id AS \"obsTypeId\", " +
            "    observation_type.name AS \"obsTypeName\", actuation_type.id AS \"actTypeId\", " +
            "    actuation_type.name AS \"actTypeName\", value " +
            "FROM property " +
            "    JOIN property_class ON \"propertyClassId\"=property_class.id " +
            "    LEFT JOIN observable_property ON property.id=observable_property.id " +
            "    LEFT JOIN actuatable_property ON property.id=actuatable_property.id " +
            "    LEFT JOIN static_property ON property.id=static_property.id " +
            "    LEFT JOIN observation_type ON observable_property.\"obsTypeId\"=observation_type.id " +
            "    LEFT JOIN actuation_type ON actuatable_property.\"actTypeId\"=actuation_type.id ";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<Property> properties = new ArrayList<>();
            while (rs.next()) {
                properties.add(new Property(rs.getInt("id"), rs.getString("name"), rs.getInt("propertyClassId"),
                    rs.getString("propertyClassName"), (Integer) rs.getObject("obsTypeId"), rs.getString("obsTypeName"),
                    (Integer) rs.getObject("actTypeId"), rs.getString("actTypeName"), rs.getString("value")));
            }

            return Res.ok(properties);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostProperty req) {
        if (req == null) return Res.nullBody();
        String q1 = "INSERT INTO property(name, \"propertyClassId\") VALUES (?, ?)";
        try {
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setString(1, req.getName());
            ps1.setInt(2, req.getPropertyClassId());
            ps1.execute();
            int id = Sql.getLastInsertId(con);

            ResultSet rs = con.prepareStatement("SELECT * FROM property_class WHERE id="+
                    req.getPropertyClassId()).executeQuery();
            if (!rs.next())
                return Res.sqlError(null, "property_class is empty");
            String className = rs.getString("name");

            if (className.equals("observable")) {
                if (req.getObsTypeId() == null)
                    return Res.bad("obsTypeId can't be null");
                con.prepareStatement("INSERT INTO observable_property(id, obsTypeId) " +
                        "VALUES (" + id + ", "+req.getObsTypeId()+")").execute();
            }
            else if (className.equals("actuatable")) {
                if (req.getActTypeId() == null)
                    return Res.bad("actTypeId can't be null");
                con.prepareStatement("INSERT INTO actuatable_property(id, actTypeId) " +
                        "VALUES (" + id + ", "+req.getActTypeId()+")").execute();
            }
            else if (className.equals("static")) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO static_property(id, value) " +
                        "VALUES (" + id + ", ?)");
                ps.setString(1, req.getValue());
                ps.execute();
            }
            else {
                return Res.error(null);
            }
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response get (Connection con, int id) {
        String q =
            "SELECT p.id, p.name AS \"name\", property_class.id AS \"propertyClassId\", " +
                    "    property_class.name AS \"propertyClassName\", observation_type.id AS \"obsTypeId\", " +
                    "    observation_type.name AS \"obsTypeName\", actuation_type.id AS \"actTypeId\", " +
                    "    actuation_type.name AS \"actTypeName\", value " +
            "FROM (SELECT * FROM property WHERE id="+id+") AS p "+
            "    JOIN property_class ON \"propertyClassId\"=property_class.id " +
            "    LEFT JOIN observable_property ON p.id=observable_property.id " +
            "    LEFT JOIN actuatable_property ON p.id=actuatable_property.id " +
            "    LEFT JOIN static_property ON p.id=static_property.id " +
            "    LEFT JOIN observation_type ON observable_property.\"obsTypeId\"=observation_type.id " +
            "    LEFT JOIN actuation_type ON actuatable_property.\"actTypeId\"=actuation_type.id ";

        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            if (!rs.next())
                return Res.notFound();
            return Res.ok(new Property(rs.getInt("id"), rs.getString("name"), rs.getInt("propertyClassId"),
                    rs.getString("propertyClassName"), (Integer) rs.getObject("obsTypeId"), rs.getString("obsTypeName"),
                    (Integer) rs.getObject("actTypeId"), rs.getString("actTypeName"), rs.getString("value")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int id, PutProperty req) {
        if (req == null) return Res.nullBody();
        try {
            ResultSet rs = con.prepareStatement("SELECT * FROM property JOIN property_class " +
                    "ON \"propertyClassId\"=property_class.id WHERE property.id="+id).executeQuery();
            if (!rs.next())
                return Res.notFound();
            String className = rs.getString("property_class.name");

            if (className.equals("observable")) {
                if (req.getObsTypeId() == null)
                    return Res.bad("obsTypeId can't be null");
                con.prepareStatement("UPDATE observable_property SET obsTypeId="+req.getObsTypeId()+
                        " WHERE id="+id).execute();
            }
            else if (className.equals("actuatable")) {
                if (req.getActTypeId() == null)
                    return Res.bad("actTypeId can't be null");
                con.prepareStatement("UPDATE actuatable_property SET actTypeId="+req.getActTypeId()+
                        " WHERE id="+id).execute();
            }
            else if (className.equals("static")) {
                con.prepareStatement("UPDATE static_property SET value="+req.getValue()+
                        " WHERE id="+id).execute();
            }
            else {
                return Res.error(null);
            }
            return Res.ok(id);

        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "property", "id", id) == 0)
                return Res.notFound();
            return Res.ok("property deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response getClasses (Connection con) {
        String query = "SELECT * FROM property_class";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<PropertyClass> classes = new ArrayList<>();
            while (rs.next())
                classes.add(new PropertyClass(rs.getInt("id"), rs.getString("name")));

            return Res.ok(classes);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

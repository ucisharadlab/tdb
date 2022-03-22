package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.entityType.*;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class EntityTypeLogic {

    public static Response get (Connection con) {
        String q = "SELECT * FROM entity_type a JOIN entity_class ON entityClassId=entity_class.id " +
                "LEFT JOIN entity_type_hierarchy ON childEntityType=a.id AND level=1 " +
                "LEFT JOIN entity_type b ON parentEntityType=b.id";
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            ArrayList<EntityTypeMini> result = new ArrayList<>();
            String q2;

            while (rs.next()) {
                int id = rs.getInt("a.id");
                q2 = "SELECT * FROM (SELECT * FROM entity_type_property WHERE entityTypeId=%d) AS a " +
                        "JOIN property ON property.id=propertyId";
                q2 = String.format(q2, id);
                ResultSet rs2 = con.prepareStatement(q2).executeQuery();

                ArrayList<EntityTypeProperty> properties = new ArrayList<>();
                while (rs2.next())
                    properties.add(new EntityTypeProperty(rs2.getInt("propertyId"), rs2.getString("name")));
                result.add(new EntityTypeMini(rs.getInt("a.id"), rs.getString("a.name"), (Integer) rs.getObject("b.id"),
                        rs.getString("b.name"), properties));
            }
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostEntityType req) {
        if (req == null) return Res.nullBody();
        String q1 = "INSERT INTO entity_type(name, entityClassId) VALUES (?, (" +
            "SELECT entityClassId FROM (SELECT * FROM entity_type) b WHERE id=?))";
        String q2 = "INSERT INTO entity_type_hierarchy(parentEntityType, childEntityType, level) " +
            "SELECT parentEntityType, ?, level+1 FROM entity_type_hierarchy " +
            "WHERE childEntityType=?";
        String q3 = "INSERT INTO entity_type_hierarchy(parentEntityType, childEntityType, level) " +
                "VALUES (?, ?, 1)";
        String q4 = "INSERT INTO entity_type_property VALUES %s";

        try {
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setString(1, req.getName());
            ps1.setInt(2, req.getSupertypeId());
            ps1.execute();
            int id = Sql.getLastInsertId(con);

            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setInt(1, id);
            ps2.setInt(2, req.getSupertypeId());
            ps2.execute();

            PreparedStatement ps3 = con.prepareStatement(q3);
            ps3.setInt(1, req.getSupertypeId());
            ps3.setInt(2, id);
            ps3.execute();

            if (req.getProperties() != null && req.getProperties().size() != 0) {
                q4 = String.format(q4, String.join(", ",
                        Collections.nCopies(req.getProperties().size(), "(" + id + ", ?)")));
                PreparedStatement ps4 = con.prepareStatement(q4);
                for (int i = 1; i <= req.getProperties().size(); ++i)
                    ps4.setInt(i, req.getProperties().get(i-1));
                ps4.execute();
            }

            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response get (Connection con, int id) {
        String q = "SELECT * FROM entity_type JOIN entity_class ON entityClassId=entity_class.id " +
                "WHERE entity_type.id="+id;
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            if (!rs.next())
                return Res.notFound();
            ArrayList<EntityTypeProperty> properties = new ArrayList<>();
            ArrayList<EntityTypeBasic> subtypes = new ArrayList<>();
            ArrayList<EntityTypeBasic> supertypes = new ArrayList<>();

            String q2 = "SELECT * FROM (SELECT * FROM entity_type_property WHERE entityTypeId=%d) AS a " +
                    "JOIN property ON property.id=propertyId";
            q2 = String.format(q2, id);
            ResultSet rs2 = con.prepareStatement(q2).executeQuery();
            while (rs2.next())
                properties.add(new EntityTypeProperty(rs2.getInt("propertyId"), rs2.getString("name")));

            String q3 = "SELECT * FROM (" +
                "   SELECT * FROM entity_type_hierarchy WHERE childEntityType=%d OR parentEntityType=%d" +
                ") AS a JOIN entity_type c ON c.id=childEntityType " +
                    "JOIN entity_type p ON p.id=parentEntityType ";
            q3 = String.format(q3, id, id);
            ResultSet rs3 = con.prepareStatement(q3).executeQuery();
            while (rs3.next()) {
                int parentType = rs3.getInt("parentEntityType");
                int childType = rs3.getInt("childEntityType");
                if (parentType == id)
                    subtypes.add(new EntityTypeBasic(childType, rs3.getString("c.name"), rs3.getInt("level")));
                else if (childType == id)
                    supertypes.add(new EntityTypeBasic(parentType, rs3.getString("p.name"), rs3.getInt("level")));
                else
                    return Res.error(null, "");
            }

            return Res.ok(new EntityType(id, rs.getString("entity_type.name"), subtypes, supertypes, properties));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int id, PutEntityType req) {
        if (req == null) return Res.nullBody();
        String q1 = "UPDATE entity_type SET name=? WHERE id=?";
        String q2 = "INSERT INTO entity_type_property(entityTypeId,propertyId) VALUES %s";
        try {
            ResultSet rs = con.prepareStatement("SELECT * FROM entity_type WHERE id="+id).executeQuery();
            if (!rs.next())
                return Res.notFound();
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setString(1, req.getName());
            ps1.setInt(2, id);
            ps1.execute();

            con.prepareStatement("DELETE FROM entity_type_property WHERE entityTypeId="+id).execute();
            if (req.getProperties() != null && req.getProperties().size() != 0) {
                PreparedStatement ps2 = con.prepareStatement(
                        String.format(q2, String.join(",",
                                Collections.nCopies(req.getProperties().size(), "(" + id + ", ?)"))));
                for (int i = 1; i <= req.getProperties().size(); ++i)
                    ps2.setInt(i, req.getProperties().get(i - 1));
                ps2.execute();
            }

            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "entity_type", "id", id) == 0)
                return Res.notFound();
            return Res.ok("entity type deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

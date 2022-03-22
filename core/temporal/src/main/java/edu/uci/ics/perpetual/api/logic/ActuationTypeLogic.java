package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.actuationType.ActuationType;
import edu.uci.ics.perpetual.api.models.actuationType.PostActuationType;
import edu.uci.ics.perpetual.api.models.actuationType.PutActuationType;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActuationTypeLogic {
    public static Response get (Connection con) {
        String q = "SELECT * FROM actuation_type";
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            ArrayList<ActuationType> result = new ArrayList<>();
            while (rs.next())
                result.add(new ActuationType(rs.getInt("id"), rs.getString("name")));
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostActuationType req) {
        if (req == null) return Res.nullBody();
        String q = "INSERT INTO actuation_type(name) VALUES (?)";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, req.getName());
            ps.execute();
            return Res.ok(Sql.getLastInsertId(con));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response get (Connection con, int id) {
        String q = "SELECT * FROM actuation_type WHERE id="+id;
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            return Res.ok(new ActuationType(rs.getInt("id"), rs.getString("name")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int id, PutActuationType req) {
        if (req == null) return Res.nullBody();
        String q = "UPDATE actuation_type SET name=? WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, req.getName());
            ps.execute();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "actuation_type", "id", id) == 0)
                return Res.notFound();
            return Res.ok("actuation type deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

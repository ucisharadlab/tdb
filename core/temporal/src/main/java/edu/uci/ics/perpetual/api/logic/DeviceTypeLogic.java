package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.deviceType.DeviceType;
import edu.uci.ics.perpetual.api.models.deviceType.PostDeviceType;
import edu.uci.ics.perpetual.api.models.deviceType.PutDeviceType;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeviceTypeLogic {

    public static Response get (Connection con) {
        String q = "SELECT * FROM device_type t JOIN device_class c " +
                "ON c.id=deviceClassId";
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            ArrayList<DeviceType> result = new ArrayList<>();
            while (rs.next())
                result.add(new DeviceType(rs.getInt("t.id"), rs.getString("t.name"),
                        rs.getInt("c.id"), rs.getString("c.name")));
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostDeviceType req) {
        if (req == null) return Res.nullBody();
        String q = "INSERT INTO device_type(name, \"deviceClassId\") VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, req.getName());
            ps.setInt(2, req.getDeviceClassId());
            ps.execute();
            return Res.ok(Sql.getLastInsertId(con));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response get (Connection con, int id) {
        String q = "SELECT * FROM device_type t JOIN device_class c " +
                "ON c.id=\"deviceClassId\" WHERE t.id="+id;
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            if (!rs.next())
                return Res.notFound();
            return Res.ok(new DeviceType(rs.getInt("t.id"), rs.getString("t.name"),
                    rs.getInt("c.id"), rs.getString("c.name")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int id, PutDeviceType req) {
        if (req == null) return Res.nullBody();
        String q = "UPDATE device_type SET name=? WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, req.getName());
            ps.setInt(2, id);
            ps.execute();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "device_type", "id", id) == 0)
                return Res.notFound();
            return Res.ok("device type deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

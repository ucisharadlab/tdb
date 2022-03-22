package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.device.Device;
import edu.uci.ics.perpetual.api.models.device.DeviceClass;
import edu.uci.ics.perpetual.api.models.device.PostDevice;
import edu.uci.ics.perpetual.api.models.device.PutDevice;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeviceLogic {

    public static Response get (Connection con, String orderBy, String direction,
                                String orderBy2, String direction2, int limit, int offset,
                                String name) {
        if (!(direction.equals("asc") || direction.equals("desc")) ||
                !(direction2.equals("asc") || direction2.equals("desc")) ||
                limit < 0 || limit > 1000 || offset < 0)
            return Res.bad("direction and direction2 must be 'asc' or 'desc'. "+
                    "limit must be be between 0-1000. offset must be positive.");
        String q = "SELECT * FROM device JOIN device_type ON \"deviceTypeId\"=device_type.id " +
                "JOIN device_class ON \"deviceClassId\"=device_class.id " +
                "WHERE device.name LIKE ? " +
                "ORDER BY ? %s, ? %s LIMIT %d OFFSET %d";
        q = String.format(q, direction, direction2, offset, limit);

        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, "%" + name + "%");
            ps.setString(2, orderBy);
            ps.setString(3, orderBy2);
            ResultSet rs = ps.executeQuery();
            ArrayList<Device> result = new ArrayList<>();

            while (rs.next())
                result.add(new Device(rs.getInt("device.id"), rs.getString("device.name"),
                    rs.getInt("deviceClassId"), rs.getString("device_class.name"),
                    rs.getInt("deviceTypeId"), rs.getString("device_type.name")));
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostDevice req) {
        if (req == null) return Res.nullBody();
        String q1 = "SELECT * FROM (SELECT * FROM device_type WHERE id="+req.getDeviceTypeId()+
                ") AS d JOIN device_class ON d.\"deviceClassId\"";
        try {
            ResultSet rs1 = con.prepareStatement(q1).executeQuery();
            if (!rs1.next())
                return Res.bad("invalid deviceTypeId");
            String className = rs1.getString("device_class.name");

            String q2 = "INSERT INTO device(name, \"deviceTypeId\") VALUES (?, ?)";
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setString(1, req.getName());
            ps2.setInt(2, req.getDeviceTypeId());
            ps2.execute();
            int id = Sql.getLastInsertId(con);

            con.prepareStatement("INSERT INTO "+className+"(id) VALUES ("+id+")").execute();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response get (Connection con, int id) {
        String q = "SELECT * FROM device JOIN device_type ON \"deviceTypeId\"=device_type.id " +
                "JOIN device_class ON \"deviceClassId\"=device_class.id WHERE device.id="+id;
        try {
            ResultSet rs = con.prepareStatement(q).executeQuery();
            if (!rs.next())
                return Res.notFound();
            return Res.ok(new Device(rs.getInt("device.id"), rs.getString("device.name"),
                    rs.getInt("deviceClassId"), rs.getString("device_class.name"),
                    rs.getInt("deviceTypeId"), rs.getString("device_type.name")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int id, PutDevice req) {
        if (req == null) return Res.nullBody();
        String q1 = "SELECT * FROM device JOIN device_type ON \"deviceTypeId\"=device_type.id " +
                "JOIN device_class ON \"deviceClassId\"=device_class.id WHERE device.id="+id;
        String q2 = "SELECT * FROM (SELECT * FROM device_type WHERE id="+req.getDeviceTypeId()+
                ") AS d JOIN device_class ON d.\"deviceClassId\"";
        try {
            ResultSet rs = con.prepareStatement("SELECT * FROM device WHERE id="+id).executeQuery();
            if (!rs.next()) return Res.notFound();

            ResultSet rs1 = con.prepareStatement(q1).executeQuery();
            ResultSet rs2 = con.prepareStatement(q2).executeQuery();
            if (!rs1.next()) return Res.error(null);
            if (!rs2.next()) return Res.bad("invalid device type id");
            if (!rs1.getString("device_class.name").equals(rs2.getString("device_class.name")))
                return Res.bad("cannot switch between device classes");

            PreparedStatement ps3 = con.prepareStatement("UPDATE device SET name=?, \"deviceTypeId\"=? " +
                    "WHERE id="+id);
            ps3.setString(1, req.getName());
            ps3.setInt(2, req.getDeviceTypeId());
            ps3.execute();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "device", "id", id) == 0)
                return Res.notFound();
            return Res.ok("device deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response getClasses (Connection con) {
        String query = "SELECT * FROM device_class";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            ArrayList<DeviceClass> classes = new ArrayList<>();
            while (rs.next())
                classes.add(new DeviceClass(rs.getInt("id"), rs.getString("name")));

            return Res.ok(classes);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

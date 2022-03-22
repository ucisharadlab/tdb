package edu.uci.ics.perpetual.api.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.perpetual.api.models.observationType.ObsType;
import edu.uci.ics.perpetual.api.models.observationType.PayloadAttribute;
import edu.uci.ics.perpetual.api.models.observationType.PostObsType;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObservationTypeLogic {

    private static String getSqlType (String type) {
        switch (type) {
            case "string":
                return "VARCHAR(200)";
            case "int":
                return "INT";
            case "float":
                return "FLOAT";
            case "boolean":
                return "BOOLEAN";
            case "timestamp":
                return "TIMESTAMP";
            default:
                return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    public static Response get (Connection con) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM observation_type");
            ResultSet rs = ps.executeQuery();
            ArrayList<ObsType> obsTypes = new ArrayList<>();

            while (rs.next()) {
                ArrayList<Map<String, String>> newFormatPayload = new ArrayList<>();
                Map<String, String> payload = mapper.readValue(rs.getString("payload"), Map.class);
                for (String key : payload.keySet()) {
                    HashMap<String, String> payloadEntry = new HashMap<>();
                    payloadEntry.put("name", key);
                    payloadEntry.put("type", payload.get(key));
                    newFormatPayload.add(payloadEntry);
                }
                obsTypes.add(new ObsType(rs.getInt("id"), rs.getString("name"),
                        newFormatPayload, rs.getBoolean("isSemantic")));
            }

            return Res.ok(obsTypes);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e, "IOException when mapping payload JSON string to java map.");
        }
    }

    public static Response post (Connection con, PostObsType req) {
        ObjectMapper mapper = new ObjectMapper();

        if (req == null) return Res.nullBody();

        // validate table name
        String name = req.getName();
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (!(c >= 65 && c <= 90) && !(c >= 97 && c <= 122) && c != 95 )
                return Res.bad("Observation type name can only contain characters a-z, A-Z, and underscore (_)");
        }

        // construct payload column MySQL query
        ArrayList<String> cols = new ArrayList<>();
        for (PayloadAttribute entry : req.getPayload()) {
            String k = entry.getName();
            String v = entry.getType();
            if (k.equals("id") || k.equals("timestamp") || k.equals("deviceId") || getSqlType(v) == null)
                return Res.bad("Invalid payload attributes. Entries must be in the form {name: type}. " +
                    "{name} cannot be 'id', 'timestamp', or 'deviceId', and {type} must be "+
                    "'string', 'int', 'float', 'boolean', or 'timestamp'.");
            cols.add(String.format("%s %s, ", k, getSqlType(v)));
        }
        String joinedCols = String.join("", cols);

        // create table
        String q1 =
            "CREATE TABLE %s_observation (" +
            "    id         int primary key not null auto_increment, " +
            "    timestamp  timestamp default current_timestamp not null, " +
            "    deviceId   int null, " +
                 joinedCols +
            "    foreign key (deviceId) references device(id) " +
            "        on update cascade " +
            ")";
        q1 = String.format(q1, name);
        try {
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.execute();
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }

        // insert into observation_type
        String q2 = "INSERT INTO observation_type(name,payload,isSemantic) VALUES (?,?,?)";
        try {
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setString(1, name);

            HashMap<String, String> newFormatPayload = new HashMap<>();
            for (PayloadAttribute entry : req.getPayload())
                newFormatPayload.put(entry.getName(), entry.getType());
            ps2.setString(2, mapper.writeValueAsString(newFormatPayload));
            ps2.setBoolean(3, req.getIsSemantic());
            ps2.execute();

            return Res.ok(Sql.getLastInsertId(con));
        }
        catch (SQLException e) {
            return Res.sqlError(e, "Error with inserting into observation_type. PLEASE CONTACT ADMIN.");
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return Res.bad("Error with converting payload into JSON string. PLEASE CONTACT ADMIN.");
        }
    }

    public static Response get (Connection con, int id) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM observation_type WHERE id="+id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return Res.notFound();

            ArrayList<Map<String, String>> newFormatPayload = new ArrayList<>();
            Map<String, String> payload = mapper.readValue(rs.getString("payload"), Map.class);
            for (String key : payload.keySet()) {
                HashMap<String, String> payloadEntry = new HashMap<>();
                payloadEntry.put("type", payload.get(key));
                payloadEntry.put("name", key);
                newFormatPayload.add(payloadEntry);
            }
            return Res.ok(new ObsType(rs.getInt("id"), rs.getString("name"),
                    newFormatPayload, rs.getBoolean("isSemantic")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e, "IOException when mapping payload JSON string to java map.");
        }
    }

    public static Response delete (Connection con, int id) {
        String q1 = "SELECT * FROM observation_type WHERE id="+id;
        String q2 = "DROP TABLE %s_observation";
        try {
            PreparedStatement ps1 = con.prepareStatement(q1);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next())
                return Res.notFound();
            String name = rs.getString("name");

            q2 = String.format(q2, name);

            // delete observation_type entry
            Sql.deleteEntry(con, "observation_type", "id", id);

            // drop observation table
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.execute();
            return Res.ok(String.format("Successfully deleted observation type '%s' and dropped table '%s_observation'",
                    name, name));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

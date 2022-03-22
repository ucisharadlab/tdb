package edu.uci.ics.perpetual.api.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.perpetual.api.models.observation.Obs;
import edu.uci.ics.perpetual.api.models.observation.PatchObs;
import edu.uci.ics.perpetual.api.models.observation.PostObs;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ObservationLogic {

    private static Map<String, String> getPayloadCols (Connection con, int obsTypeId)
            throws SQLException, IOException {
        PreparedStatement ps = con.prepareStatement("SELECT payload FROM observation_type "+
                "WHERE id=" + obsTypeId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(rs.getString("payload"), Map.class);
    }

    private static String getTableName (Connection con, int obsTypeId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT name FROM observation_type "+
                "WHERE id=" + obsTypeId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;
        return String.format("%s_observation", rs.getString("name"));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static Response get (Connection con, int obsTypeId, String orderBy, String direction,
                                String orderBy2, String direction2, int limit, int offset,
                                Timestamp before, Timestamp after, UriInfo uriInfo) {
        if (before == null)
            before = new Timestamp(Long.MAX_VALUE);
        if (after == null)
            after = new Timestamp(0);
        if (!(direction.equals("asc") || direction.equals("desc")) ||
            !(direction2.equals("asc") || direction2.equals("desc")) ||
            limit < 0 || limit > 1000 || offset < 0)
            return Res.bad("direction and direction2 must be 'asc' or 'desc'. "+
                    "limit must be be between 0-1000. offset must be positive.");

        try {
            String tableName = getTableName(con, obsTypeId);
            if (tableName == null)
                return Res.notFound();

            // strip uriInfo query params
            List<String> fixedQueryParams = Arrays.asList("orderBy", "direction",
                    "orderBy2", "direction2", "limit", "offset", "before", "after", "mostRecent");
            ArrayList<String> equalQueryParams = new ArrayList<>();
            // maybe have 'lessQueryParams' and 'moreQueryParams' later?
            for (String key : uriInfo.getQueryParameters().keySet()) {
                if (fixedQueryParams.indexOf(key) == -1)
                    equalQueryParams.add(String.format("%s IN (%s)", key,
                            String.join(",", uriInfo.getQueryParameters().get(key))));
            }

            String conditionals = "";
            if (equalQueryParams.size() > 0)
                conditionals = String.format("AND %s", String.join(" AND ", equalQueryParams));

            Map<String, String> payloadCols = getPayloadCols(con, obsTypeId);
            String q;
            ResultSet rs;
            q = "SELECT * FROM (SELECT * FROM %s WHERE timestamp < ? AND timestamp > ? " +
                    conditionals + " ORDER BY %s %s, %s %s LIMIT %d OFFSET %d " +
                    ") AS a LEFT JOIN device ON a.deviceId=device.id";
            q = String.format(q, tableName, orderBy, direction, orderBy2, direction2, offset, limit);

            PreparedStatement ps = con.prepareStatement(q);
            ps.setTimestamp(1, before);
            ps.setTimestamp(2, after);
            rs = ps.executeQuery();


            List<Obs> result = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> payload = new HashMap<>();
                for (String key : payloadCols.keySet()) {
                    payload.put(key, rs.getObject("a."+key));
                }

                result.add(new Obs(rs.getInt("a.id"), rs.getTimestamp("timestamp"),
                        (Integer) rs.getObject("deviceId"), rs.getString("device.name"), payload));
            }
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    public static Response post (Connection con, int obsTypeId, List<PostObs> req) {
        ObjectMapper mapper = new ObjectMapper();
        if (req == null) return Res.nullBody();
        try {
            // get obs table name and payload
            PreparedStatement ps = con.prepareStatement("SELECT name, payload FROM observation_type "+
                    "WHERE id=" + obsTypeId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Res.notFound();
            Map<String, Object> payload = mapper.readValue(rs.getString("payload"), Map.class);

            // insert observation
            String[] keys = new String[payload.size()+2];
            String[] keysWithTicks = new String[payload.size()+2];
            keys[0] = "timestamp";   keysWithTicks[0] = "timestamp";
            keys[1] = "deviceId";    keysWithTicks[1] = "deviceId";
            int i = 2;
            for (String key : payload.keySet()) {
                keys[i] = key;
                keysWithTicks[i++] = String.format("%s", key);
            }

            String oneValue = String.format("(%s)", String.join(",", Collections.nCopies(i, "?")));

            String q = String.format("INSERT INTO %s_observation(%s) VALUES %s",
                    rs.getString("name"), 
                    String.join(",", keysWithTicks),
                    String.join(",", Collections.nCopies(req.size(), oneValue)));
            PreparedStatement ps2 = con.prepareStatement(q);

            for (int j = 0; j < req.size(); ++j)
                for (int k = 0; k < keys.length; ++k) {
                    if (k == 0) ps2.setTimestamp(keys.length * j + 1, req.get(j).getTimestamp());
                    else if (k == 1) ps2.setObject(keys.length * j + 2, req.get(j).getDeviceId());
                    else
                        ps2.setObject(keys.length * j + k + 1, req.get(j).getPayload().get(keys[k]));
                }

            ps2.execute();
            return Res.ok("Observation value(s) successfully inserted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    public static Response get (Connection con, int obsTypeId, int id) {
        try {
            String tableName = getTableName(con, obsTypeId);
            if (tableName == null)
                return Res.notFound();
            Map<String, String> payloadCols = getPayloadCols(con, obsTypeId);
            String q = "SELECT * FROM (SELECT * FROM %s WHERE id=%d) AS a " +
                    "LEFT JOIN device ON deviceId=device.id";
            q = String.format(q, tableName, id);

            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return Res.notFound();

            Map<String, Object> payload = new HashMap<>();
            for (String key : payloadCols.keySet())
                payload.put(key, rs.getObject("a."+key));

            return Res.ok(new Obs(rs.getInt("a.id"), rs.getTimestamp("timestamp"),
                    (Integer) rs.getObject("deviceId"), rs.getString("name"), payload));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    public static Response patch (Connection con, int obsTypeId, int id, PatchObs req) {
        ObjectMapper mapper = new ObjectMapper();
        if (req == null) return Res.nullBody();
        try {
            // get obs table name and payload
            PreparedStatement ps = con.prepareStatement("SELECT name FROM observation_type "+
                    "WHERE id=" + obsTypeId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Res.notFound();
            Map<String, Object> payload = mapper.readValue(rs.getString("payload"), Map.class);

            // update observation
            ArrayList<String> updateAttributes = new ArrayList<>(); // maintain order
            Map<String, Object> updateEntries = new HashMap<>();
            if (req.getTimestamp() != null) {
                updateAttributes.add("timestamp");
                updateEntries.put("timestamp", req.getTimestamp());
            }
            if (req.getDeviceId() != null) {
                updateAttributes.add("deviceId");
                updateEntries.put("deviceId", req.getDeviceId());
            }
            for (String key : req.getPayload().keySet()) {
                if (!payload.containsKey(key))
                    return Res.bad("invalid payload key");
                updateAttributes.add(key);
                updateEntries.put(key, req.getPayload().get(key));
            }
            ArrayList<String> toJoin = new ArrayList<>();
            updateAttributes.forEach((s) -> toJoin.add(String.format("%s=?", s)));

            String q = String.format("UPDATE %s_observation SET %s WHERE id=%d",
                    rs.getString("name"), String.join(",", toJoin), id);
            PreparedStatement ps2 = con.prepareStatement(q);

            int i = 1;
            for (String key : updateAttributes) {
                ps2.setObject(i++, updateEntries.get(key));
            }
            //System.out.println(ps2.toString());
            ps2.execute();
            return Res.ok("observation value updated successfully");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    public static Response delete (Connection con, int obsTypeId, int id) {
        try {
            if (Sql.deleteEntry(con, getTableName(con, obsTypeId), "id", id) == 0)
                return Res.notFound();
            return Res.ok("Successfully deleted observation");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

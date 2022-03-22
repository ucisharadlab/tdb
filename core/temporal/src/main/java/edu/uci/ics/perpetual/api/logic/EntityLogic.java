package edu.uci.ics.perpetual.api.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.perpetual.api.models.entity.EntityMini;
import edu.uci.ics.perpetual.api.models.entity.PostEntity;
import edu.uci.ics.perpetual.api.models.entity.PutEntity;
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

public class EntityLogic {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Map<String, Object> getEntityMiniPayload (Connection con, int id, String className, int depth, boolean fullDetail)
            throws IOException, SQLException {

        Map<String, Object> payload;
        if (className.equals("person")) {
            payload = null;
        }
        else if (className.equals("portal")) {
            payload = new HashMap<>();
            String q = "SELECT * FROM portal WHERE id=" + id;
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return null;
            payload.put("startSpaceId", rs.getInt("startSpaceId"));
            payload.put("endSpaceId", rs.getInt("endSpaceId"));
            payload.put("startExtent", mapper.readValue(rs.getString("startExtentId"), Map.class));
            payload.put("endExtent", mapper.readValue(rs.getString("endExtentId"), Map.class));
        }
        else if (className.equals("space")) {
            payload = new HashMap<>();
            Map<String, Object> geo = new HashMap<>();
            ArrayList<Object> children = new ArrayList<Object>();
            String q = "SELECT * FROM ( \n" +
                       "    (SELECT * FROM space WHERE id="+id+") AS a " +
                       "    JOIN geo_parent_space gps ON a.geoId=gps.geoId AND level=1 " +
                       "    JOIN geo ON gps.geoId=geo.id)";
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                payload.put("geo", null);
                return payload;
            }

            if(fullDetail)
            {
            	String q2 = "select a.id, space.id 'childSpaceId' from ( " +
                        "    (select * from space where id="+id+") as a " +
                        "    join geo_parent_space gps on parentSpaceId=a.id " +
                        "    join space on gps.geoId=space.geoId " +
                        ") where level=1;";
	            ResultSet rs2 = con.prepareStatement(q2).executeQuery();
	            while (rs2.next()) 
	            	children.add(get(con, rs2.getInt("childSpaceId"), depth, fullDetail));
            }
            else if (depth > 0) {
                String q2 = "select a.id, space.id 'childSpaceId' from ( " +
                            "    (select * from space where id="+id+") as a " +
                            "    join geo_parent_space gps on parentSpaceId=a.id " +
                            "    join space on gps.geoId=space.geoId " +
                            ") where level=1;";
                ResultSet rs2 = con.prepareStatement(q2).executeQuery();
//                ArrayList<EntityMini> childSpaces = new ArrayList<>();
                while (rs2.next())
                	children.add(get(con, rs2.getInt("childSpaceId"), depth - 1, false));
//                    childSpaces.add(get(con, rs2.getInt("childSpaceId"), depth - 1, false));
//                geo.put("childSpaces", childSpaces);
            }
            else if (depth <= 0)
            {
            }

            geo.put("parentSpaceId", rs.getInt("parentSpaceId"));
            geo.put("extent", mapper.readValue(rs.getString("extent"), Map.class));
            geo.put("coordinateSystem", mapper.readValue(rs.getString("coordinateSystem"), Map.class));
            payload.put("geo", geo);
            if (depth != 0 || fullDetail)
            {
            	payload.put("children", children);
            }
            
        }
        else {
            payload = null;
        }

        return payload;
    }

    /* TODO */
    private static boolean validatePayload (String className, Map<String, Object> pl) {
        if (className.equals("person"))
            return pl == null;
        else if (className.equals("portal")) {
            if (!(pl.get("startSpaceId") instanceof Integer) ||
                !(pl.get("endSpaceId") instanceof Integer) ||
                !(pl.get("startExtent") instanceof Map || pl.get("startExtent") == null) ||
                !(pl.get("endExtent") instanceof Map   || pl.get("endExtent") == null))
                return false;
        }
        else if (className.equals("space")) {
            if (pl.containsKey("geo") && pl.get("geo") == null)
                return true;
            if (!pl.containsKey("geo") || !(pl.get("geo") instanceof Map))
                return false;
            try {
                Map<String, Object> geo = (Map) pl.get("geo");
                if (geo.containsKey("extent") && geo.containsKey("coordinateSystem") &&
                    geo.get("extent") == null && geo.get("coordinateSystem") == null)
                    return true;
                if (!(geo.get("parentSpaceId") instanceof Integer) ||
                    !(geo.get("extent") instanceof Map) || !(geo.get("coordinateSystem") instanceof Map))
                    return false;
                Map<String, Object> extent = (Map) geo.get("extent");
                Map<String, Object> coordinateSystem = (Map) geo.get("coordinateSystem");
                if (!(extent.get("extentClassName") instanceof String) ||
                    !(coordinateSystem.get("coordinateSystemClassName") instanceof String))
                    return false;
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    private static boolean validateNestedPayload (String className, Map<String, Object> pl) {
        if (className.equals("person"))
            return pl == null;
        else if (className.equals("portal")) {
            if (!(pl.get("startSpaceId") instanceof Integer) ||
                !(pl.get("endSpaceId") instanceof Integer) ||
                !(pl.get("startExtent") instanceof Map || pl.get("startExtent") == null) ||
                !(pl.get("endExtent") instanceof Map   || pl.get("endExtent") == null))
                return false;
        }
        else if (className.equals("space")) {
            if (!pl.containsKey("geo") || !(pl.get("geo") instanceof Map))
                return false;
            try {
                Map<String, Object> geo = (Map) pl.get("geo");
                if (//!(geo.get("parentSpaceId") instanceof Integer) ||
                    !(geo.get("extent") instanceof Map) || !(geo.get("coordinateSystem") instanceof Map))
                    return false;
                Map<String, Object> extent = (Map) geo.get("extent");
                Map<String, Object> coordinateSystem = (Map) geo.get("coordinateSystem");
                if (!(extent.get("extentClassName") instanceof String) ||
                    !(coordinateSystem.get("coordinateSystemClassName") instanceof String))
                    return false;
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    

    private static void insertPayload (Connection con, String className, int id, Map<String, Object> payload)
            throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String q3;
        int parentSpaceId = 0;
        if (className.equals("person")) {
            q3 = "INSERT INTO person(id) VALUES (" + id + ")";
            con.prepareStatement(q3).execute();
        }
        else if (className.equals("portal")) {
            q3 = "INSERT INTO portal(id, startSpaceId, endSpaceId, " +
                    "startExtent, endExtent) VALUES (%d, %d, ?, ?)";
            q3 = String.format(q3, payload.get("startSpaceId"),
                    payload.get("endSpaceId"));
            PreparedStatement ps3 = con.prepareStatement(q3);
            ps3.setString(1, mapper.writeValueAsString(payload.get("startExtent")));
            ps3.setString(2, mapper.writeValueAsString(payload.get("endExtent")));
            ps3.execute();
        }
        else if (className.equals("space")) {
            q3 = "INSERT INTO geo(extent, coordinateSystem) VALUES (?, ?)";
            PreparedStatement ps3 = con.prepareStatement(q3);
            Map<String, Object> geo = (Map) payload.get("geo");
            ps3.setString(1, mapper.writeValueAsString(geo.get("extent")));
            ps3.setString(2, mapper.writeValueAsString(geo.get("coordinateSystem")));
            ps3.execute();
            int geoId = Sql.getLastInsertId(con);
            parentSpaceId = (int) geo.get("parentSpaceId");

            con.prepareStatement("INSERT INTO space(id, geoId) VALUES ("+id+", "+geoId+")").execute();

            // insert levels
            String q4 = "INSERT INTO geo_parent_space "+
                    "SELECT "+geoId+", parentSpaceId, level+1 FROM geo_parent_space " +
                    "WHERE geoId=(SELECT geoId FROM space WHERE id="+parentSpaceId+")";

            con.prepareStatement(q4).execute();
            con.prepareStatement("INSERT INTO geo_parent_space VALUES ("+geoId+","+parentSpaceId+",1)")
                    .execute();
        }
        if (payload.containsKey("children"))
        {
        	ArrayList<Object> child = (ArrayList<Object>) payload.get("children");
        	for (int i = 0; i < child.size(); i++)
        	{
        		Map<String, Object> m = (Map<String, Object>) child.get(i);
        		insertNestedEntity(con, id, m);
        	}
        	
        }
    }
    private static void insertPayloadNested (Connection con, String className, int id, Map<String, Object> payload)
            throws SQLException, JsonProcessingException {
        String q3;
        int parentSpaceId = 0;
        if (className.equals("person")) {
            q3 = "INSERT INTO person(id) VALUES (" + id + ")";
            con.prepareStatement(q3).execute();
        }
        else if (className.equals("portal")) {
            q3 = "INSERT INTO portal(id, startSpaceId, endSpaceId, " +
                    "startExtent, endExtent) VALUES (%d, %d, ?, ?)";
            q3 = String.format(q3, payload.get("startSpaceId"),
                    payload.get("endSpaceId"));
            PreparedStatement ps3 = con.prepareStatement(q3);
            ps3.setString(1, mapper.writeValueAsString(payload.get("startExtent")));
            ps3.setString(2, mapper.writeValueAsString(payload.get("endExtent")));
            ps3.execute();
        }
        else if (className.equals("space")) {
            q3 = "INSERT INTO geo(extent, coordinateSystem) VALUES (?, ?)";
            PreparedStatement ps3 = con.prepareStatement(q3);
            Map<String, Object> geo = (Map) payload.get("geo");
            ps3.setString(1, mapper.writeValueAsString(geo.get("extent")));
            ps3.setString(2, mapper.writeValueAsString(geo.get("coordinateSystem")));
            ps3.execute();
            int geoId = Sql.getLastInsertId(con);
            parentSpaceId = (int) payload.get("parentSpaceId");

            con.prepareStatement("INSERT INTO space(id, geoId) VALUES ("+id+", "+geoId+")").execute();

            // insert levels
            String q4 = "INSERT INTO geo_parent_space "+
                    "SELECT "+geoId+", parentSpaceId, level+1 FROM geo_parent_space " +
                    "WHERE geoId=(SELECT geoId FROM space WHERE id="+parentSpaceId+")";

            con.prepareStatement(q4).execute();
            con.prepareStatement("INSERT INTO geo_parent_space VALUES ("+geoId+","+parentSpaceId+",1)")
                    .execute();
        }
        if (payload.containsKey("children"))
        {
        	ArrayList<Object> child = (ArrayList<Object>) payload.get("children");
        	for (int i = 0; i < child.size(); i++)
        	{
        		Map<String, Object> m = (Map<String, Object>) child.get(i);
        		insertNestedEntity(con, id, m);
        	}
        	
        }
    }
    
    private static void insertNestedEntity(Connection con, int parentId, Map<String, Object> m)
    {
    	if (!m.containsKey("name") || !m.containsKey("entityTypeId") || !m.containsKey("payload"))
    	{
    		return;
    	}
    	String name = (String) m.get("name");
        int entityTypeId = (int) m.get("entityTypeId");
        Map<String, Object> payload = (Map<String, Object>) m.get("payload");
    	String q = "SELECT entity_class.name 'name' FROM entity_type JOIN entity_class ON " +
                "entityClassId=entity_class.id WHERE entity_type.id=?";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, entityTypeId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return;
            
            /* TODO */
            String className = rs.getString("name");
            if (!validateNestedPayload(className, payload))
                return;
            String q2 = "INSERT INTO entity(name, entityTypeId) VALUES (?, ?)";
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setString(1, name);
            ps2.setInt(2, entityTypeId);
            ps2.execute();
            int id = Sql.getLastInsertId(con);
            payload.put("parentSpaceId", parentId);
            
            insertPayloadNested(con, className, id, payload);
        }
        catch (SQLException e) {
        }
        catch (JsonProcessingException e) {
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Response get (Connection con, String orderBy, String direction,
                                String orderBy2, String direction2, int limit, int offset) {
        if (!(direction.equals("asc") || direction.equals("desc")) ||
                !(direction2.equals("asc") || direction2.equals("desc")) ||
                limit < 0 || limit > 3000 || offset < 0)
            return Res.bad("direction and direction2 must be 'asc' or 'desc'. "+
                    "limit must be be between 0-3000. offset must be positive.");

        String q = "SELECT entity.id \"id\", entity.name \"name\", \"entityTypeId\", entity_type.name " +
                "\"entityTypeName\", \"entityClassId\", entity_class.name \"entityClassName\" FROM entity "+
                "JOIN entity_type ON \"entityTypeId\"=entity_type.id " +
                "JOIN entity_class ON \"entityClassId\"=entity_class.id " +
                "ORDER BY ? %s, ? %s LIMIT %d OFFSET %d";

        try {
            q = String.format(q, direction, direction2, offset, limit);
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, orderBy);
            ps.setString(2, orderBy2);
            ResultSet rs = ps.executeQuery();
            int id;
            String className;

            ArrayList<EntityMini> result = new ArrayList<>();
            while (rs.next()) {

                // figure out payload
                id = rs.getInt("id");
                className = rs.getString("entityClassName");
                Map<String, Object> payload = getEntityMiniPayload(con, id, className, 0, false);

                result.add(new EntityMini(rs.getInt("id"), rs.getString("name"), rs.getInt("entityClassId"),
                        rs.getString("entityClassName"), rs.getInt("entityTypeId"),
                        rs.getString("entityTypeName"), payload));
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

    public static Response post (Connection con, PostEntity req) {
        if (req == null) return Res.nullBody();
        String q = "SELECT entity_class.name 'name' FROM entity_type JOIN entity_class ON " +
                "entityClassId=entity_class.id WHERE entity_type.id=?";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, req.getEntityTypeId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Res.bad("Invalid entityTypeId");

            /* TODO */
            String className = rs.getString("name");
            if (!validatePayload(className, req.getPayload()))
                return Res.bad("Payload incorrectly formatted. Please refer to the docs");

            String q2 = "INSERT INTO entity(name, entityTypeId) VALUES (?, ?)";
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setString(1, req.getName());
            ps2.setInt(2, req.getEntityTypeId());
            ps2.execute();
            int id = Sql.getLastInsertId(con);

            insertPayload(con, className, id, req.getPayload());

            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (JsonProcessingException e) {
            return Res.error(e);
        }
    }
    
    
    public static EntityMini get (Connection con, int id, int depth, boolean fullDetail) throws SQLException, IOException {
        String q = "SELECT * FROM (SELECT * FROM entity WHERE id=?) AS e " +
                "JOIN entity_type ON entityTypeId=entity_type.id " +
                "JOIN entity_class ON entityClassId=entity_class.id " +
                "LEFT JOIN person ON e.id=person.id LEFT JOIN portal ON e.id=portal.id " +
                "LEFT JOIN space ON e.id=space.id LEFT JOIN geo ON geoId=geo.id";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return null;

            String className = rs.getString("entity_class.name");
            Map<String, Object> payload = getEntityMiniPayload(con, id, className, depth, fullDetail);
            if (className.equals("space") && payload != null && payload.containsKey("geo"))
                ((Map) payload.get("geo")).put("geoId", rs.getInt("geoId"));

            return new EntityMini(rs.getInt("e.id"), rs.getString("e.name"),
                    rs.getInt("entityClassId"), rs.getString("entity_class.name"),
                    rs.getInt("entityTypeId"), rs.getString("entity_type.name"), payload);
    }

    public static Response put (Connection con, int id, PutEntity req) {
        if (req == null) return Res.nullBody();

        String q1 = "SELECT entity_class.name FROM (SELECT * FROM entity WHERE id=?) AS a " +
                   "JOIN entity_type ON entityTypeId=entity_type.id " +
                   "JOIN entity_class ON entityClassId=entity_class.id";
        String q2 = "SELECT entity_class.name FROM (SELECT * FROM entity_type WHERE id=?) AS a " +
                "JOIN entity_class ON entityClassId=entity_class.id";

        try {
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return Res.bad("Entity with that id does not exist");

            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setInt(1, req.getEntityTypeId());
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) return Res.bad("Invalid entityTypeId");


            String className = rs1.getString("entity_class.name");
            if (!className.equals(rs2.getString("entity_class.name")))
                return Res.bad("Cannot switch between entity classes (person, portal, space)");
            if (!validatePayload(className, req.getPayload()))
                return Res.bad("Payload badly formatted.");

            if (className.equals("person")) {
                // there's nothing to update
            }
            else if (className.equals("portal")) {
                con.prepareStatement("DELETE FROM portal WHERE id="+id).execute();
                insertPayload(con, "portal", id, req.getPayload());
            }
            else if (className.equals("space")) {
                con.prepareStatement("DELETE FROM geo WHERE id=" +
                        "(SELECT geoId 'id' FROM space WHERE id="+id+")").execute();
                insertPayload(con, "space", id, req.getPayload());
            }
            else {
                return Res.error(null);
            }

            String q3 = "UPDATE entity SET name=?, entityTypeId=? WHERE id=?";
            PreparedStatement ps3 = con.prepareStatement(q3);
            ps3.setString(1, req.getName());
            ps3.setInt(2, req.getEntityTypeId());
            ps3.setInt(3, id);
            ps3.execute();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    public static Response delete (Connection con, int id) {
        try {
            if (Sql.deleteEntry(con, "entity", "id", id) == 0)
                return Res.notFound();
            return Res.ok(id);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}

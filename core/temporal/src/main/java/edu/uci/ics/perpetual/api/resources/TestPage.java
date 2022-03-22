package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.models._other.QueryReq;
import edu.uci.ics.perpetual.api.utilities.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Api(value = "test")
@Path("test")
public class TestPage {

    @ApiOperation(value="Test endpoint, says hello")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello (@Context HttpHeaders headers)
    {
        /*String s = "";
        int r = (int) (Math.random()*100 + 2);
        int j = (int) (Math.random()*6);
        String[] tables = {"entity", "observation_type", "entity_type", "device", "device_type", "actuation_type"};
        System.out.println("computing...");
        for (int i = 0; i < 20; ++i) {
            try {
                PreparedStatement ps = Main.getCon().prepareStatement("SELECT * FROM "+tables[j]+" LIMIT " + r);
                j = (int) (Math.random()*6);
                ps.executeQuery();
                ResultSet rs = ps.executeQuery();
                rs.next();
                System.out.println(rs.getString("name"));
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("done.");*/
        return "hello!";
    }


    @ApiOperation(value="Write a SQL Query",
        notes="Query the internal database of this TIPPERS instance. This endpoint blocks any "+
                "queries that modify data (i.e. any query with 'DELETE', 'UPDATE', etc. won't work)")

    @Path("query")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response query (@Context HttpHeaders headers, @RequestBody(content=@Content(
                                schema=@Schema(implementation= QueryReq.class)))
                                QueryReq req)
    {
        Connection con;
        try {
            con = Main.getConPool().requestCon();
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }

        try {
            PreparedStatement ps = con.prepareStatement(req.getQuery());
            ResultSet rs = ps.executeQuery();
            ArrayList<String> colLabels = new ArrayList<>();
            for (int i = 1; true; ++i) {
                try { colLabels.add(rs.getMetaData().getColumnLabel(i)); }
                catch (SQLException e) { break; }
            }

            ArrayList<Map> result = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, Object> row = new HashMap<>();
                for (String label : colLabels)
                    row.put(label, rs.getObject(label));
                result.add(row);
            }
            Main.getConPool().releaseCon(con);

            return Res.ok(result);
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 0)
                return Res.bad("Cannot write queries that manipulate data.");
            return Res.sqlError(e);
        }
    }
}

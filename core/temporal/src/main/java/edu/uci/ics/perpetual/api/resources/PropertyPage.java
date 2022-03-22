package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.PropertyLogic;
import edu.uci.ics.perpetual.api.models.property.PostProperty;
import edu.uci.ics.perpetual.api.models.property.PutProperty;
import edu.uci.ics.perpetual.api.utilities.Authentication;
import edu.uci.ics.perpetual.api.utilities.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.SQLException;

@Api(value = "Property")
@Path("property")
public class PropertyPage {

    @ApiOperation(
            value="search for properties",
            notes="")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        try {
            Connection con = Main.getConPool().requestCon();
            Response res = PropertyLogic.get(con);
            Main.getConPool().releaseCon(con);
            return res;
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    @ApiOperation(value="add a property")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                         schema=@Schema(implementation= PostProperty.class)))
                                 PostProperty req)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        try {
            Connection con = Main.getConPool().requestCon();
            Response res = PropertyLogic.post(con, req);
            Main.getConPool().releaseCon(con);
            return res;
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    @ApiOperation(value="retrieve info of 1 property")
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        try {
            Connection con = Main.getConPool().requestCon();
            Response res = PropertyLogic.get(con, id);
            Main.getConPool().releaseCon(con);
            return res;
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    @ApiOperation(value="update a property")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update (@Context HttpHeaders headers,
                            @PathParam("id") int id,
                            @RequestBody(content=@Content(
                            schema=@Schema(implementation= PutProperty.class)))
                                    PutProperty req)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = PropertyLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete a property")
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = PropertyLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of all property classes")
    @Path("class")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClasses (@Context HttpHeaders headers) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = PropertyLogic.getClasses(con);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

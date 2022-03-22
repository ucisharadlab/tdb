package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.UserLogic;
import edu.uci.ics.perpetual.api.models.user.PostUser;
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

@Api(value = "User")
@Path("user")
public class UserPage {

    @ApiOperation(
            value="search for users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers,
                         @QueryParam("email") @DefaultValue("") String email,
                         @QueryParam("authId") @DefaultValue("0") int authId,
                         @QueryParam("orderBy") @DefaultValue("id") String orderBy,
                         @QueryParam("direction") @DefaultValue("asc") String direction,
                         @QueryParam("orderBy2") @DefaultValue("id") String orderBy2,
                         @QueryParam("direction2") @DefaultValue("asc") String direction2,
                         @QueryParam("limit") @DefaultValue("200") int limit,
                         @QueryParam("offset") @DefaultValue("0") int offset)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = UserLogic.get(con, email, authId, orderBy, direction,
                orderBy2, direction2, limit, offset);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="add a user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation= PostUser.class)))
                                 PostUser req)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = UserLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of 1 user")
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = UserLogic.get(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="update a user")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update (@Context HttpHeaders headers,
                            @PathParam("id") int id,
                            @RequestBody(content=@Content(
                                    schema=@Schema(implementation= PostUser.class)))
                                        PostUser req)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = UserLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete a user")
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = UserLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

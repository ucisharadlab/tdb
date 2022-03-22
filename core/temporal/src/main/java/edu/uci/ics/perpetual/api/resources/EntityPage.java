package edu.uci.ics.perpetual.api.resources;


import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.EntityLogic;
import edu.uci.ics.perpetual.api.models.entity.EntityMini;
import edu.uci.ics.perpetual.api.models.entity.PostEntity;
import edu.uci.ics.perpetual.api.models.entity.PutEntity;
import edu.uci.ics.perpetual.api.utilities.Authentication;
import edu.uci.ics.perpetual.api.utilities.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Api(value = "Entity")
@Path("entity")
public class EntityPage {

    @ApiOperation(
        value="search for entities",
        notes="")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers,
                         @QueryParam("orderBy") @DefaultValue("id") String orderBy,
                         @QueryParam("direction") @DefaultValue("asc") String direction,
                         @QueryParam("orderBy2") @DefaultValue("id") String orderBy2,
                         @QueryParam("direction2") @DefaultValue("asc") String direction2,
                         @QueryParam("limit") @DefaultValue("200") int limit,
                         @QueryParam("offset") @DefaultValue("0") int offset)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityLogic.get(con, orderBy, direction,
                orderBy2, direction2, limit, offset);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="add an entity")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation=PostEntity.class)))
                                 PostEntity req)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of an entity, set detail to true if all children info desired")
    @Path("{id}/{depth}/{detail}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers, @PathParam("id") int id, @PathParam("depth") int depth, @PathParam("detail") boolean detail) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        try {
            EntityMini res = EntityLogic.get(con, id, depth, detail);

            Main.getConPool().releaseCon(con);

            if (res == null)
                return Res.notFound();
            return Res.ok(res);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
        catch (IOException e) {
            return Res.error(e);
        }
    }

    @ApiOperation(value="update an entity")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update (@Context HttpHeaders headers,
                            @PathParam("id") int id,
                            @RequestBody(content=@Content(
                            schema=@Schema(implementation=PutEntity.class)))
                                    PutEntity req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete an entity")
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

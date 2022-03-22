package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.EntityTypeLogic;
import edu.uci.ics.perpetual.api.models.entity.PostEntity;
import edu.uci.ics.perpetual.api.models.entityType.PostEntityType;
import edu.uci.ics.perpetual.api.models.entityType.PutEntityType;
import edu.uci.ics.perpetual.api.utilities.Authentication;
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
import java.sql.SQLException;

@Api(value = "Entity Type")
@Path("entity/type")
public class EntityTypePage {

    @ApiOperation(value="retrieve info of all entity types")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityTypeLogic.get(con);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="add new entity type")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response post (@Context HttpHeaders headers,
                          @RequestBody(content=@Content(
                                  schema=@Schema(implementation= PostEntity.class)))
                                  PostEntityType req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityTypeLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of all entity types")
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityTypeLogic.get(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="add new entity type")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put (@Context HttpHeaders headers,
                          @PathParam("id") int id,
                          @RequestBody(content=@Content(
                                  schema=@Schema(implementation=PutEntityType.class)))
                                  PutEntityType req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityTypeLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete entity type")
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = EntityTypeLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

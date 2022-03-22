package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.ObservationTypeLogic;
import edu.uci.ics.perpetual.api.models.observationType.PostObsType;
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

@Api(value="Observation Type")
@Path("observation/type")
public class ObservationTypePage {

    @ApiOperation(value="retrieve info of all observation types")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationTypeLogic.get(con);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="add an observation type")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post (@Context HttpHeaders headers,
                             @RequestBody(content=@Content(
                                     schema=@Schema(implementation= PostObsType.class)))
                                     PostObsType req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationTypeLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of all observation types")
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

        Response res = ObservationTypeLogic.get(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete an observation type")
    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers,
                            @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationTypeLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

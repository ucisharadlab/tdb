package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.ObservationLogic;
import edu.uci.ics.perpetual.api.models.observation.PatchObs;
import edu.uci.ics.perpetual.api.models.observation.PostObs;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Observation")
@Path("observation")
public class ObservationPage {

    @ApiOperation(
        value="search for observation values",
        notes="")
    @Path("{obsTypeId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers,
                         @PathParam("obsTypeId") int obsTypeId,
                         @QueryParam("orderBy") @DefaultValue("id") String orderBy,
                         @QueryParam("direction") @DefaultValue("asc") String direction,
                         @QueryParam("orderBy2") @DefaultValue("id") String orderBy2,
                         @QueryParam("direction2") @DefaultValue("asc") String direction2,
                         @QueryParam("limit") @DefaultValue("200") int limit,
                         @QueryParam("offset") @DefaultValue("0") int offset,
                         @QueryParam("before") /* max timestamp */ Timestamp before,
                         @QueryParam("after") /* min timestamp */ Timestamp after,
                         @Context UriInfo uriInfo)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationLogic.get(con, obsTypeId,
                    orderBy, direction, orderBy2, direction2, limit, offset,
                    before, after, uriInfo);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(
            value="add an observation value",
            notes="<b>WARNING:</b> when adding observation values with a specific timestamp, "+
                    "please specify timestamp in this format yyyy-mm-ddThh:mm-{offset} where "+
                    "{offset} is the timezone offset. For example, if you want to add an observation "+
                    "value with timestamp March 24 2020 11:00 AM PST, write <b>2020-03-24T11:00-07:00</b>")
    @Path("{obsTypeId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @PathParam("obsTypeId") int obsTypeId,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation= PostObs.class)))
                                 PostObs req) {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;

        int i;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        ArrayList<PostObs> reqList = new ArrayList<>();
        reqList.add(req);
        Response res = ObservationLogic.post(con, obsTypeId, reqList);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(
            value="add many observation values",
            notes="<b>WARNING:</b> when adding observation values with a specific timestamp, "+
                    "please specify timestamp in this format yyyy-mm-ddThh:mm-{offset} where "+
                    "{offset} is the timezone offset. For example, if you want to add an observation "+
                    "value with timestamp March 24 2020 11:00 AM PST, write <b>2020-03-24T11:00-07:00</b>")
    @Path("{obsTypeId}/many")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMany (@Context HttpHeaders headers,
                         @PathParam("obsTypeId") int obsTypeId,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation= List.class)))
                                 List<PostObs> req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationLogic.post(con, obsTypeId, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of 1 observation value")
    @Path("{obsTypeId}/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne (@Context HttpHeaders headers,
                         @PathParam("obsTypeId") int obsTypeId,
                         @PathParam("id") int id)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationLogic.get(con, obsTypeId, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of 1 observation value")
    @Path("{obsTypeId}/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch (@Context HttpHeaders headers,
                         @PathParam("obsTypeId") int obsTypeId,
                         @PathParam("id") int id,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation= PatchObs.class)))
                                   PatchObs req) {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationLogic.patch(con, obsTypeId, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete an observation value")
    @Path("{obsTypeId}/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers,
                            @PathParam("obsTypeId") int obsTypeId,
                            @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = ObservationLogic.delete(con, obsTypeId, id);
        Main.getConPool().releaseCon(con);
        return res;
    }


}

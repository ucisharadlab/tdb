package edu.uci.ics.perpetual.api.resources;


import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.DeviceTypeLogic;
import edu.uci.ics.perpetual.api.models.deviceType.PostDeviceType;
import edu.uci.ics.perpetual.api.models.deviceType.PutDeviceType;
import edu.uci.ics.perpetual.api.utilities.Authentication;
import edu.uci.ics.perpetual.api.utilities.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.SQLException;

@Api(value = "Device Type")
@Path("device/type")
public class DeviceTypePage {

    @ApiOperation(
            value="get device types",
            notes="")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceTypeLogic.get(con);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value="add a device",
            notes="Adds a device when provided a name and deviceTypeId.")
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation= PostDeviceType.class)))
                                 PostDeviceType req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceTypeLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(
            value="retrieve info of 1 device type",
            notes="Retrieves info of 1 device type given the ID as a path parameter.")
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne (@Context HttpHeaders headers,
                         @PathParam("id")
                         @ApiParam(value="ID of the device you're looking for")
                                 int id)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceTypeLogic.get(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="update a device")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update (@Context HttpHeaders headers,
                            @PathParam("id") int id,
                            @RequestBody(content=@Content(
                                    schema=@Schema(implementation= PutDeviceType.class)))
                                    PutDeviceType req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceTypeLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete a device type")
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

        Response res = DeviceTypeLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }
}

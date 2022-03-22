package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.DeviceLogic;
import edu.uci.ics.perpetual.api.models.device.PostDevice;
import edu.uci.ics.perpetual.api.models.device.PutDevice;
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

@Api(value = "Device")
@Path("device")
public class DevicePage {
    @ApiOperation(
            value="search for devices",
            notes="Search for a list of devices. This endpoint takes query parameters that can filter," +
                    "sort, limit, and offset search results. \n\nNOT LISTED: id (int), name (string), " +
                    "deviceTypeId (int[]), deviceTypeName (string[]), deviceClassId (int[]), " +
                    "deviceClassName (string[])")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @QueryParam("orderBy") @DefaultValue("id") String orderBy,
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("orderBy2")  @DefaultValue("id") String orderBy2,
            @QueryParam("direction2") @DefaultValue("asc") String direction2,
            @QueryParam("limit") @DefaultValue("25") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("name") @DefaultValue("") String name)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceLogic.get(con,
                orderBy, direction, orderBy2, direction2, limit, offset, name);
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
                         schema=@Schema(implementation=PostDevice.class)))
                                 PostDevice req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceLogic.post(con, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(
            value="retrieve info of 1 device",
            notes="Retrieves info of 1 device given the ID as a path parameter.")
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

        Response res = DeviceLogic.get(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="update a device")
    @Path("{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put (@Context HttpHeaders headers,
                            @PathParam("id") int id,
                            @RequestBody(content=@Content(
                            schema=@Schema(implementation=PutDevice.class)))
                                    PutDevice req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        Response res = DeviceLogic.put(con, id, req);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="delete a device")
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

        Response res = DeviceLogic.delete(con, id);
        Main.getConPool().releaseCon(con);
        return res;
    }

    @ApiOperation(value="retrieve info of all device classes")
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

        Response res = DeviceLogic.getClasses(con);
        Main.getConPool().releaseCon(con);
        return res;
    }

    /*@ApiOperation(value="retrieve info of all device types")
    @Path("type/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes (@Context HttpHeaders headers) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        return DeviceLogic.getTypes(Main.getCon());
    }

    @ApiOperation(value="add a device type")
    @Path("type/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addType (@Context HttpHeaders headers,
                             @RequestBody(content=@Content(
                             schema=@Schema(implementation=dev_AddTypeReq.class)))
                                     dev_AddTypeReq req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        return DeviceLogic.addType(Main.getCon(), req.getName(), req.getDeviceClassId());
    }

    @ApiOperation(value="update a device type")
    @Path("type/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateType (@Context HttpHeaders headers,
                                @RequestBody(content=@Content(
                                schema=@Schema(implementation=dev_UpdateTypeReq.class)))
                                        dev_UpdateTypeReq req) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        return DeviceLogic.updateType(Main.getCon(), req.getId(), req.getName());
    }

    @ApiOperation(value="delete a device type")
    @Path("type/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteType (@Context HttpHeaders headers, @PathParam("id") int id) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        return DeviceLogic.deleteType(Main.getCon(), id);
    }

    @ApiOperation(value="retrieve info of all device classes")
    @Path("class/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClasses (@Context HttpHeaders headers) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null)
            return authRes;

        return DeviceLogic.getClasses(Main.getCon());
    }*/
}

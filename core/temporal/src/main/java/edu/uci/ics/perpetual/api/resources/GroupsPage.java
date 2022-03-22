package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.*;
import edu.uci.ics.perpetual.api.models.entity.*;
import edu.uci.ics.perpetual.api.models.groups.*;
import edu.uci.ics.perpetual.api.utilities.Authentication;
import edu.uci.ics.perpetual.api.utilities.Res;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.SQLException;

@Api(value = "Groups")
@Path("Groups")
public class GroupsPage {

    @ApiOperation(
            value="search for groups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers,
                         @QueryParam("name") @DefaultValue("") String name,
                         @QueryParam("description") @DefaultValue("") String description,
                         @QueryParam("orderBy") @DefaultValue("groupId") String orderBy,
                         @QueryParam("direction") @DefaultValue("asc") String direction,
                         @QueryParam("orderBy2") @DefaultValue("groupId") String orderBy2,
                         @QueryParam("direction2") @DefaultValue("asc") String direction2,
                         @QueryParam("limit") @DefaultValue("200") int limit,
                         @QueryParam("offset") @DefaultValue("0") int offset)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.get(con, name, description, orderBy, direction,
                orderBy2, direction2, limit, offset);
    }

    @ApiOperation(value="add a group")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation=PostGroups.class)))
//    					 @ApiParam(examples = @Example(value = @ExampleProperty
//    					 					(mediaType = MediaType.APPLICATION_JSON,
//    					 					 value = "{\"name\": string"
//    					 					 		+ "\"description\": string"
//    					 					 		+ "\"visibility\": false"
//    					 					 		+ "\"members\": []}"	 	)))
                                 PostGroups req)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.post(con, req);
    }

    @ApiOperation(value="retrieve info of 1 group")
    @Path("{groupId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@Context HttpHeaders headers, @PathParam("groupId") int groupId) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.get(con, groupId);
    }

    @ApiOperation(value="update a group")
    @Path("{groupId}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update (@Context HttpHeaders headers,
                            @PathParam("groupId") int groupId,
                            @RequestBody(content=@Content(
                                    schema=@Schema(implementation= PostGroups.class)))
                                        PostGroups req)
    {
        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.put(con, groupId, req);
    }

    @ApiOperation(value="delete a group")
    @Path("{groupId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@Context HttpHeaders headers, @PathParam("groupId") int groupId) {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.delete(con, groupId);
    }
}

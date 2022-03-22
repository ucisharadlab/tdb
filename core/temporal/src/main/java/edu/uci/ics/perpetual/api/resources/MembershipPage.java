package edu.uci.ics.perpetual.api.resources;

import edu.uci.ics.perpetual.api.Main;
import edu.uci.ics.perpetual.api.logic.GroupsLogic;
import edu.uci.ics.perpetual.api.models.groups.*;
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

@Api(value = "Membership")
@Path("Membership")
public class MembershipPage {
	@ApiOperation(value="gets members of a group")
	@Path("{groupId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
  	public Response get (@Context HttpHeaders headers, @PathParam("groupId") int groupId)
	{

		Response authRes = Authentication.authenticate(headers);
		if (authRes != null) return authRes;
		
		Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

		return GroupsLogic.getMembers(con, groupId);
	}
	 
    @ApiOperation(value="add a membership")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add (@Context HttpHeaders headers,
                         @RequestBody(content=@Content(
                                 schema=@Schema(implementation=PostMembershipWithoutGroup.class)))
                                 PostMembershipWithoutGroup req)
    {

        Response authRes = Authentication.authenticate(headers);
        if (authRes != null) return authRes;
        
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }

        return GroupsLogic.postMembership(con, req);
    }

	@ApiOperation(value="delete a membership")
	@Path("{groupId}/{personId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete (@Context HttpHeaders headers, @PathParam("groupId") int groupId, @PathParam("personId") int personId)
	{
	
	    Response authRes = Authentication.authenticate(headers);
	    if (authRes != null) return authRes;
	    
	    Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {    return Res.sqlError(e); }
	
	    return GroupsLogic.delete(con, groupId, personId);
	}

}

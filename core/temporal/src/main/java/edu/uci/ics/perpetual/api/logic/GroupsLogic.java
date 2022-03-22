package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.groups.*;
import edu.uci.ics.perpetual.api.models.user.User;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupsLogic {
	
	/*********************************** Helper Methods ***************************************/
	
	public static HashMap<Integer, Membership> getMembershipByGroup(Connection con, int groupId)
	{
		String q = "select * from Membership where groupId = " + groupId;
		HashMap <Integer, Membership> members = new HashMap <Integer, Membership>();
		try
		{
			ResultSet rs = con.prepareStatement(q).executeQuery();
			while(rs.next())
			{
				int personId = rs.getInt("personId");
				members.put(personId, new Membership(rs.getInt("groupId"), personId, rs.getBoolean("role")));
			}
		}
		catch (SQLException e)
		{
		}
		return members;
	}
	
	public static HashMap<Integer, Membership> getMembershipByPerson(Connection con, int personId)
	{
		String q = "select * from Membership where groupId = " + personId;
		HashMap <Integer, Membership> members = new HashMap <Integer, Membership>();
		try
		{
			ResultSet rs = con.prepareStatement(q).executeQuery();
			while(rs.next())
			{
				members.put(personId, new Membership(rs.getInt("groupId"), personId, rs.getBoolean("role")));
			}
		}
		catch (SQLException e)
		{
		}
		return members;
	}
	
	public static void addMembers(Connection con, int groupId, PostGroups pg)
	{
		ArrayList <PostMembership> members = pg.getMembers();
		
		members.forEach(m -> {
			String q = "insert into membership (groupId, personId, role) values(?, ?, ?)";
			try
			{
				PreparedStatement ps = con.prepareStatement(q);
				ps.setInt(1, groupId);
				ps.setInt(2, m.getPersonId());
				ps.setBoolean(3, m.getRole());
				ps.execute();
			}
			catch(SQLException e)
			{
				String q1 = "Update membership Set role = ? groupId = ? and personId = ?";
				try
				{
					PreparedStatement ps = con.prepareStatement(q1);
					ps.setInt(2, groupId);
					ps.setInt(3, m.getPersonId());
					ps.setBoolean(1, m.getRole());
					ps.execute();
				}
				catch(SQLException e1)
				{
					
				}
			}
		});
		
	}
	
	/***********************************************************************************/
	
	/*
	 * gets group based on groupId
	 */
	public static Response get(Connection con, int groupId)
	{
		String q = "select * from groupsTable where groupId = " + groupId;
		try
		{
			ResultSet rs = con.prepareStatement(q).executeQuery();
			if (!rs.next())
                return Res.notFound();
			return Res.ok(new Groups( rs.getInt("groupId"), rs.getString("name"),
					rs.getString("description"), rs.getBoolean("visibility"), getMembershipByGroup(con, groupId)));
		}
		catch(SQLException e)
		{
			return Res.sqlError(e);
		}
	}
	
	/*
	 * allows the user to search for groups
	 */
	public static Response get(Connection con, String name, String description,
								String orderBy, String direction, String orderBy2,
								String direction2, int limit, int offset)
	{
		if (!(direction.equals("asc") || direction.equals("desc")) ||
                !(direction2.equals("asc") || direction2.equals("desc")) ||
                limit < 0 || limit > 1000 || offset < 0)
		{
			return Res.bad("direction and direction2 must be 'asc' or 'desc'. "+
                    "limit must be be between 0-1000. offset must be positive.");
		}
		String q = "SELECT * FROM groupsTable WHERE name LIKE ? AND " +
                "description LIKE ? ORDER BY %s %s, %s %s LIMIT %d OFFSET %d";
     q = String.format(q, orderBy, direction, orderBy2, direction2, offset, limit);
     try {
         PreparedStatement ps = con.prepareStatement(q);
         ps.setString(1, "%" + name + "%");
         ps.setString(2, "%" + description + "%");
         ResultSet rs = ps.executeQuery();
         ArrayList<Groups> results = new ArrayList<Groups>();
         while(rs.next())
         {
        	 results.add(new Groups(rs.getInt("groupId"), rs.getString("name"),
        			 		rs.getString("description"), rs.getBoolean("visibility"),
        			 		getMembershipByGroup(con, rs.getInt("groupId"))));
         }
         return Res.ok(results);
     }
     catch (SQLException e)
     {
    	 return Res.sqlError(e);
     }
            
	}
	
	/*
	 * gets the membership of a specific group
	 */
	public static Response getMembers(Connection con, int groupId)
	{
		HashMap <Integer, Membership> members = getMembershipByGroup(con, groupId);
		ArrayList<User> users = new ArrayList<User>();
		members.forEach((k, v) -> {
			String q = "Select * from User where id = "+ v.getPersonId();
			try {
				PreparedStatement ps = con.prepareStatement(q);
				ResultSet rs = ps.executeQuery();
				rs.next();
				users.add(new User(rs.getInt("id"), rs.getInt("authId"), rs.getString("name"), rs.getString("email"), rs.getBoolean("admin")));
			}
			catch(SQLException e)
			{
				System.out.println(e);
			}
		});
		
		return Res.ok(users);
	}
	
	/*
	 * adds new group to database
	 */
	public static Response post(Connection con, PostGroups pg)
	{
		String q = "insert into groupsTable(name, description, visibility) VALUES (?,?,?)";
		try
		{
			PreparedStatement ps = con.prepareStatement(q);
			ps.setString(1, pg.getName());
			ps.setString(2, pg.getDescription());
			ps.setBoolean(3, pg.getVisibility());
			ps.execute();
			int i = Sql.getLastInsertId(con);
			
			
			if(pg.getMembers() != null)
			{
				addMembers(con, i, pg);
			}
			
			
			return Res.ok(i);
		}
        catch (SQLException e) {
            return Res.sqlError(e);
        }
	}
	
	/*
	 * adds new membership to db
	 */
	public static Response postMembership(Connection con, PostMembershipWithoutGroup pg)
	{
		String q = "insert into memberhsip(groupId, personId, role) VALUES(?,?,?)";
		
		try
		{
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, pg.getGroupId());
			ps.setInt(2, pg.getPersonId());
			ps.setBoolean(3, pg.getRole());
			ps.execute();
			return Res.ok(Sql.getLastInsertId(con));
		}
		catch (SQLException e)
		{
			return Res.sqlError(e);
		}
	}
	
	/*
	 * edits the information within an existing group
	 */
	public static Response put(Connection con, int groupId, PostGroups pg)
	{
		String q = "Update groupsTable Set name = ?, description = ?, visibility = ? where groupId = " + groupId;
		
		try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, pg.getName());
            ps.setString(2, pg.getDescription());
            ps.setBoolean(3, pg.getVisibility());
            if(pg.getMembers() != null)
            {
            	addMembers(con, groupId, pg);
            }
            
            ps.execute();

            return Res.ok(groupId);
        }
		catch (SQLException e)
		{
			return Res.sqlError(e);
		}
	}
	
	
	
	/*
	 * deletes group from database
	 */
	public static Response delete(Connection con, int groupId)
	{
		try {
            if (Sql.deleteEntry(con, "groupsTable", "groupId", groupId) == 0)
                return Res.notFound();
            return Res.ok("group deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
	}
	
	
	/*
	 * delete member from database
	 */
	public static Response delete(Connection con, int groupId, int personId)
	{
		try {
            	String q = "Delete from membership where groupId = ? and personId = ?";
            	PreparedStatement ps = con.prepareStatement(q);
            	ps.setInt(1, groupId);
            	ps.setInt(2, personId);
            	int n = ps.executeUpdate();
            	if (n == 0)
            	{
            		return Res.notFound();
            	}
            return Res.ok("membership deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
	}
}

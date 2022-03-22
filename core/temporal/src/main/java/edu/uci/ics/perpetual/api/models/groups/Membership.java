package edu.uci.ics.perpetual.api.models.groups;

public class Membership {
	
	private int groupId;
	private int personId;
	private boolean role;
	
	public Membership(int groupId, int personId, boolean role)
	{
		this.groupId = groupId;
		this.personId = personId;
		this.role = role;
	}
	
	public int getGroupId()
	{
		return this.groupId;
	}
	
	public int getPersonId()
	{
		return this.personId;
	}
	
	public boolean getRole()
	{
		return this.role;
	}
	
	public void setRole(boolean role)
	{
		this.role = role;
	}
	
}

package edu.uci.ics.perpetual.api.models.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Groups {
	
	private int groupId;
	private String name;
	private String description;
	private boolean visibility;
	private HashMap<Integer, Membership> members;

	
	public Groups(int groupId, String name, String description, boolean visibility, HashMap<Integer, Membership> members)
	{
		this.groupId = groupId;
		this.name = name;
		this.description = description;
		this.visibility = visibility;
		this.members = members;
	}
	
	public int getGroupId()
	{
		return this.groupId;
	}
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public boolean getVisibility()
	{
		return this.visibility;
	}
	
	public HashMap<Integer, Membership> getMembers()
	{
		return this.members;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setVisibility(boolean visibility)
	{
		this.visibility = visibility;
	}
	
	public boolean addMember(Membership member)
	{
		if(!members.containsKey(member.getPersonId()))
		{
			this.members.put(member.getPersonId(), member);
			return true;
		}
		
		return false;
	}
	
	public boolean removeMember(Membership member)
	{
		if(members.containsKey(member.getPersonId()))
		{
			members.remove(member.getPersonId());
			return true;
		}
		return false;
	}
}

package edu.uci.ics.perpetual.api.models.groups;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = "dataValid")

public class PutGroups {

	private int groupId;
	private String name;
	private String description;
	private boolean visibility;
	private Map<Integer, PostMembership> members;
	
	public PutGroups(
					@JsonProperty(value = "groupId", required = true) int groupId,
					@JsonProperty(value = "name", required = true) String name,
					@JsonProperty(value = "description", required = true) String description,
					@JsonProperty(value = "visibility", required = true) boolean visibility,
					@JsonProperty(value = "members") Map<Integer, PostMembership> members
					)
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
	
	public Map<Integer, PostMembership> getMembers()
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
}









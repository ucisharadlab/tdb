package edu.uci.ics.perpetual.api.models.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")

public class PostGroups {

	private String name;
	private String description;
	private boolean visibility;
	private ArrayList<PostMembership> members;
	
	public PostGroups(
					@JsonProperty(value = "name", required = true) String name,
					@JsonProperty(value = "description", required = true) String description,
					@JsonProperty(value = "visibility", required = true) boolean visibility,
					@JsonProperty(value = "members") ArrayList<PostMembership> members
					)
	{
		this.name = name;
		this.description = description;
		this.visibility = visibility;
		this.members = members;
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
	
	public ArrayList<PostMembership> getMembers()
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









package edu.uci.ics.perpetual.api.models.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;

@JsonIgnoreProperties(value = "dataValid")
public class PostMembershipWithoutGroup {
	private int groupId;
	private int personId;
	private boolean role;
	
	public PostMembershipWithoutGroup(@JsonProperty(value = "groupId", required = true) int groupId,
						@JsonProperty(value = "personId", required = true) int personId,
						@JsonProperty(value = "role", required = true) boolean role)
	{
		this.groupId = groupId;
		this.personId = personId;
		this.role = role;
	}
	
	@ApiModelProperty(position = 1,required = true)
	public int getGroupId()
	{
		return this.groupId;
	}
	@ApiModelProperty(position = 2,required = true)
	public int getPersonId()
	{
		return this.personId;
	}
	@ApiModelProperty(position = 3,required = true)
	public boolean getRole()
	{
		return this.role;
	}
}

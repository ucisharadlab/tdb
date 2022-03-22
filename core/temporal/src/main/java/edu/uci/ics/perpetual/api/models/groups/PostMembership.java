package edu.uci.ics.perpetual.api.models.groups;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(value = "dataValid")
public class PostMembership {
//	private int groupId;
	private int personId;
	private boolean role;
	
	public PostMembership(@JsonProperty(value = "personId", required = true) @JacksonInject(value = "0") int personId,
						@JsonProperty(value = "role", required = true) @JacksonInject(value = "false") boolean role)
	{
		this.personId = personId;
		this.role = role;
	}
	
//	public int getGroupId()
//	{
//		return this.groupId;
//	}
	@ApiModelProperty(position = 1,required = true)
	public int getPersonId()
	{
		return this.personId;
	}
	@ApiModelProperty(position = 1,required = true)
	public boolean getRole()
	{
		return this.role;
	}
}

package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;


/*
@JsonProperty("id") String instanceId,
@JsonProperty("start_date") Date startDate,
@JsonProperty("last_change_date") Date lastChangeDate,
@JsonProperty("pattern_id")


*/
@XmlRootElement(name = "instance")
public class InstanceBP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3970402595042367862L;

	enum InstanceState { RUNNING, ABORT , TERMINATED, WAITING, PAUSE};

	private String instanceId = null;

	// Id of the real instance on the real bpe
	private String bpeId = null;

	private Date startDate = null;
	
	private Date lastChangeDate = null;

	private String patternId = null;
	//TODO ActivityBP
	private List<Integer> currentActivities = null;

	private List<ActionLink>links = null;
	// TODO change type
	private List<PatternHole> holes = null;
	@XmlTransient
	@JsonIgnore
	// users of api are not supposed to know it or use it
	public String getBpeId() {
		return bpeId;
	}
	@JsonIgnore
	public void setBpeId(String bpeId) {
		this.bpeId = bpeId;
	}

	@JsonIgnore
	@XmlTransient
	private String templateId = null;
	@JsonIgnore
	public String getTemplateId() {
		return templateId;
	}
	@JsonIgnore
	@XmlTransient
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	@JsonIgnore // This field is read only
	public void setState(InstanceBP.InstanceState state) {
		
	}
	
	@JsonGetter ("id")
	public String getInstanceId() {
		return instanceId;
	}
	@JsonSetter ("id")
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getLastChangeDate() {
		return lastChangeDate;
	}
	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	public String getPatternId() {
		return patternId;
	}
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}
	public List<Integer> getCurrentActivities() {
		// TODO call WS!
		return currentActivities;
	}
	@XmlElementWrapper(name="currentActivities")
	@XmlElement(name="activity")
	@JsonIgnore
	@JsonSetter("current_activities")
	public void setCurrentActivities(List<Integer> currentActivities) {
		this.currentActivities = currentActivities;
	}
	@XmlElementWrapper(name="links")
	@XmlElement(name="link")
	public List<ActionLink> getLinks() {
		return links;
	}
	public List<PatternHole> getHoles() {
		return holes;
	}
	@XmlElementWrapper(name="holes")
	@XmlElement(name="hole")
	public void setHoles(List<PatternHole> holes) {
		this.holes = holes;
	}

	
	@Override
	public String toString() {
		return "InstanceBP [instanceId=" + instanceId + ", startDate=" + startDate + ", lastChangeDate="
				+ lastChangeDate + ", patternId=" + patternId + ", currentActivities=" + currentActivities + ", links="
				+ links + ", holes=" + holes + ", templateId=" + templateId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceBP other = (InstanceBP) obj;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		return true;
	}
	
	public InstanceBP(String instanceId, Date startDate, Date lastChangeDate, String patternId,
			List<Integer> currentActivities, List<PatternHole> holes) {
		super();
		this.instanceId = instanceId;
		this.startDate = startDate;
		this.lastChangeDate = lastChangeDate;
		this.patternId = patternId;
		this.currentActivities = currentActivities;
		this.holes = holes;
		this.links = generateLinks();
	}

	private List<ActionLink> generateLinks() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@XmlElement(name="state")
	public InstanceState getState(){
		// TODO ADD LOGIC
		if ( holes == null ) {
			return InstanceState.RUNNING;
		}
		for (PatternHole aHole: holes) {
			if (( aHole.getPatternAssigned() == null ) || ( aHole.getPatternAssigned().isEmpty() ) )
				return InstanceState.RUNNING;
		}
		return InstanceState.RUNNING;
	}
	
	@JsonCreator
	public InstanceBP(
			@JsonProperty("id") String instanceId,
			@JsonProperty("start_date") Date startDate,
			@JsonProperty("last_change_date") Date lastChangeDate,
			@JsonProperty("pattern_id") String patternId)
	{
		super();
		this.instanceId = instanceId;
		this.startDate = startDate;
		this.lastChangeDate = lastChangeDate;
		this.patternId = patternId;
		this.links = generateLinks();
	}
	
	public InstanceBP(
			String instanceId,
			Date startDate,
			Date lastChangeDate,
			String patternId,
			List<PatternHole> holes,
			String templateId)
	{
		super();
		this.instanceId = instanceId;
		this.startDate = startDate;
		this.lastChangeDate = lastChangeDate;
		this.patternId = patternId;
		setHolesFromPattern(holes);
		this.links = generateLinks();
		this.templateId = templateId;
	}
	
	public InstanceBP() {
		super();
	}
	private void setHolesFromPattern(List<PatternHole> holes) {
		// TODO
		this.holes = holes;
/*		if ( holes == null ) {
			this.holes = null;
			return;
		}
		if ( this.id == null) {
			System.out.println("WOW NULL!!!");
		}
		this.holes = new ArrayList<PatternHole>();
		for ( Hole aHole : holes ) {
			if ( aHole == null) {
				System.out.println("WOW1 NULL!!!");
			}
			this.holes.add (new PatternHole(aHole, this.id));
		}
		*/
	}
}

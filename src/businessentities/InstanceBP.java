package businessentities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;


@XmlRootElement(name = "instance")
public class InstanceBP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3970402595042367862L;

	private InstanceState state = null;

	private String instanceId = null;

	// Id of the real instance on the real BPE
	private String bpeId = null;

	private Date startDate = null;
	
	private Date lastChangeDate = null;

	private String patternId = null;
	//TODO ActivityBP
	private List<Integer> currentActivities = null;

	private List<ActionLink>links = null;
	// TODO change type
	private List<InstanceHole> holes = null;
	
	@JsonIgnore
	@XmlTransient
	private String templateId = null;

	@JsonIgnore
	@XmlTransient
	// users of API are not supposed to know it or use it
	public String getBpeId() {
		return bpeId;
	}
	@JsonIgnore
	public void setBpeId(String bpeId) {
		this.bpeId = bpeId;
	}

	@JsonIgnore
	public String getTemplateId() {
		return templateId;
	}
	@JsonIgnore
	@XmlTransient
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	@JsonSetter ("state")
	@XmlElement(name = "state")
	public void setState(InstanceState state) {
		this.state = state;
	}
	
	@JsonGetter ("id")
	public String getInstanceId() {
		return instanceId;
	}
	
	@JsonSetter ("id")
	@XmlElement(name = "id")
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	@JsonGetter ("start_date")
	public Date getStartDate() {
		return startDate;
	}
	
	@JsonSetter ("start_date")
	@XmlElement(name = "start_date")
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonGetter ("last_change_date")
	public Date getLastChangeDate() {
		return lastChangeDate;
	}
	
	@JsonSetter ("last_change_date")
	@XmlElement(name = "last_change_date")
	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	
	@JsonGetter ("pattern_id")
	public String getPatternId() {
		return patternId;
	}
	
	@JsonSetter ("pattern_id")
	@XmlElement(name = "pattern_id")
	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}
	
	@JsonGetter("current_activities")
	public List<Integer> getCurrentActivities() {
		// TODO call WS!
		return currentActivities;
	}
	
	@JsonSetter("current_activities")
	@XmlElementWrapper(name="current_activities")
	@XmlElement(name="activity")
	public void setCurrentActivities(List<Integer> currentActivities) {
		this.currentActivities = currentActivities;
	}

	@JsonGetter ("links")
	@XmlElementWrapper(name="links")
	@XmlElement(name="link")
	public List<ActionLink> getLinks() {
		return links;
	}
	@JsonGetter ("holes")
	public List<InstanceHole> getHoles() {
		return holes;
	}
	
	@JsonSetter ("holes")
	@XmlElementWrapper(name="holes")
	@XmlElement(name="hole")
	public void setHoles(List<InstanceHole> holes) {
		this.holes = holes;
	}

	@Override
	public String toString() {
		return "InstanceBP [state=" + state + ", instanceId=" + instanceId + ", bpeId=" + bpeId + ", startDate="
				+ startDate + ", lastChangeDate=" + lastChangeDate + ", patternId=" + patternId + ", currentActivities="
				+ currentActivities + ", links=" + links + ", holes=" + holes + ", templateId=" + templateId + "]";
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
			List<Integer> currentActivities, List<InstanceHole> holes) {
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
	
	@JsonGetter("state")
	public InstanceState getState(){
		return state;
		// TODO ADD LOGIC
		/*if ( state.equals(InstanceState.ABORTED) || state.equals(InstanceState.TERMINATED)) {
			return state;
		}
		if ( holes == null ) {
			return InstanceState.RUNNING;
		}
		for (PatternHole aHole: holes) {
			if (( aHole.getPatternAssigned() == null ) || ( aHole.getPatternAssigned().isEmpty() ) )
				return InstanceState.RUNNING;
		}
		return InstanceState.RUNNING;*/
	}
	
	
	public InstanceBP(
			String instanceId,
			Date startDate,
			Date lastChangeDate,
			String patternId)
	{
		super();
		this.instanceId = instanceId;
		this.startDate = startDate;
		this.lastChangeDate = lastChangeDate;
		this.patternId = patternId;
		this.links = generateLinks();
		this.state = InstanceState.RUNNING;
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
		this.state = InstanceState.RUNNING;
	}
	
	public InstanceBP() {
		super();
	}

	@JsonIgnore
	@XmlTransient
	private void setHolesFromPattern(List<PatternHole> holes) {
		if ( holes == null ) {
			this.holes = null;
			return;
		}
		this.holes = new ArrayList<InstanceHole>();
		for ( PatternHole aHole : holes ) {
			this.holes.add (new InstanceHole(aHole, null));
		}
	}
}

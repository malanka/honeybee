package businessentities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "instancehole")
public class InstanceHole extends Hole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3014274278261206558L;

	// state of the assigned instance
	private InstanceState instanceState = null;
	
	// Id of the instance assigned to this hole
	private String instanceId = null;
	
	private String parentInstanceId = null;

	private String patternAssigned = null;
	@JsonGetter("parent_instance_id")
	public String getParentInstanceId() {
		return parentInstanceId;
	}
	@JsonSetter("parent_instance_id")
	@XmlElement(name="parent_instance_id")
	public void setParentInstanceId(String parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}
	@JsonGetter("pattern_assigned_id")
	public String getPatternAssigned() {
		return patternAssigned;
	}
	@JsonSetter("pattern_assigned_id")
	@XmlElement(name="pattern_assigned_id")
	public void setPatternAssigned(String patternAssigned) {
		this.patternAssigned = patternAssigned;
	}

	@JsonGetter("state")
	public InstanceState getInstanceState() {
		return instanceState;
	}

	@JsonSetter("state")
	@XmlElement(name="state")
	public void setInstanceState(InstanceState instanceState) {
		this.instanceState = instanceState;
	}

	@JsonGetter("instance_id")
	public String getInstanceId() {
		return instanceId;
	}

	@JsonSetter("instance_id")
	@XmlElement(name="instance_id")
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public InstanceHole(String name, String data_in, String data_out, String event_start, String event_end,
			String patternInstanceId, String patternAssigned, List<ActionLink> links) {
		super(name, data_in, data_out, event_start, event_end);
		this.instanceState = InstanceState.NOTSTARTED;
		this.patternAssigned = patternAssigned;
		this.parentInstanceId = patternInstanceId;
		// TODO links
	}

	public InstanceHole() {
		super();
		this.instanceState = InstanceState.NOTSTARTED;
	}

	public InstanceHole(PatternHole patternHole, String instanceId, String parentInstanceId) {
		super(patternHole.getName(), patternHole.getData_in(), patternHole.getData_out(), patternHole.getEvent_start(),
				patternHole.getEvent_end());
		this.instanceId = instanceId;
		this.instanceState = InstanceState.NOTSTARTED;
		this.patternAssigned = patternHole.getPatternAssigned();
		this.parentInstanceId = parentInstanceId;
		// TODO links
	}

	public InstanceHole(PatternHole patternHole, String parentInstanceId) {
		super(patternHole.getName(), patternHole.getData_in(), patternHole.getData_out(), patternHole.getEvent_start(),
				patternHole.getEvent_end());
		this.instanceId = null;
		this.instanceState = InstanceState.NOTSTARTED;
		this.patternAssigned = patternHole.getPatternAssigned();
		this.parentInstanceId = parentInstanceId;
		// TODO links
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
		result = prime * result + ((instanceState == null) ? 0 : instanceState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceHole other = (InstanceHole) obj;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (instanceState != other.instanceState)
			return false;
		return true;
	}

	public boolean compareWith(PatternHole obj) {
		System.out.println("PatternHole=" + obj);
		System.out.println("InstanceHole=" + this);
		if (obj == null)
			return false;
		if (getData_in() == null) {
			if (obj.getData_in() != null)
				return false;
		} else if (!getData_in().equals(obj.getData_in()))
			return false;
		if (getData_out() == null) {
			if (obj.getData_out() != null)
				return false;
		} else if (!getData_out().equals(obj.getData_out()))
			return false;
		if (getEvent_end() == null) {
			if (obj.getEvent_end() != null)
				return false;
		} else if (!getEvent_end().equals(obj.getEvent_end()))
			return false;
		if (getEvent_start() == null) {
			if (obj.getEvent_start() != null)
				return false;
		} else if (!getEvent_start().equals(obj.getEvent_start()))
			return false;
		if (getName() == null) {
			if (obj.getName() != null)
				return false;
		} else if (!getName().equals(obj.getName()))
			return false;
		if (getPatternAssigned() == null) {
			if (obj.getPatternAssigned() != null)
				return false;
		} else if (!getPatternAssigned().equals(obj.getPatternAssigned()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InstanceHole [instanceId=" + instanceId + ", toString()=" + super.toString() +"]";
	}
	
	
}
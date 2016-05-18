package businessentities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "instancehole")
public class InstanceHole extends PatternHole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3014274278261206558L;

	// state of the assigned instance
	private InstanceState instanceState = null;
	
	// Id of the instance assigned to this hole
	private String instanceId = null;

	
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
			String patternParent, String patternAssigned, List<ActionLink> links) {
		super(name, data_in, data_out, event_start, event_end, patternParent, patternAssigned, links);
		this.instanceState = InstanceState.NOTSTARTED;
		// TODO links
	}

	public InstanceHole() {
		super();
		this.instanceState = InstanceState.NOTSTARTED;
	}

	public InstanceHole(PatternHole patternHole, String instanceId) {
		super(patternHole, patternHole.getPatternParent(), patternHole.getPatternAssigned());
		this.instanceId = instanceId;
		this.instanceState = InstanceState.NOTSTARTED;
		// TODO links
	}

	public InstanceHole(PatternHole patternHole) {
		super(patternHole, patternHole.getPatternParent(), patternHole.getPatternAssigned());
		this.instanceId = null;
		this.instanceState = InstanceState.NOTSTARTED;
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
		if (this == obj)
			return true;
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
		if (getPatternParent() == null) {
			if (obj.getPatternParent() != null)
				return false;
		} else if (!getPatternParent().equals(obj.getPatternParent()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InstanceHole [instanceId=" + instanceId + ", toString()=" + super.toString() +"]";
	}
	
	
}
package businessentities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "instancehole")
public class InstanceHole extends PatternHole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3014274278261206558L;

	// Id of the instance assigned to this hole
	private String instanceId = null;

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
		// TODO links
	}

	public InstanceHole() {
		super();
	}

	public InstanceHole(PatternHole patternHole, String instanceId) {
		super(patternHole, patternHole.getPatternParent(), patternHole.getPatternAssigned());
		this.instanceId = instanceId;
		// TODO links
	}

	public InstanceHole(PatternHole patternHole) {
		super(patternHole, patternHole.getPatternParent(), patternHole.getPatternAssigned());
		this.instanceId = null;
		// TODO links
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
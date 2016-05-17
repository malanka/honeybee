package businessentities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "patternhole")
public class PatternHole extends Hole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8767355844366707476L;

	private String patternParent = null;

	private String patternAssigned = null;

	private List<ActionLink>links = null;

	public PatternHole(String name, String data_in, String data_out, String event_start, String event_end,
			String patternParent, String patternAssigned, List<ActionLink> links) {
		super(name, data_in, data_out, event_start, event_end);
		this.patternParent = patternParent;
		this.patternAssigned = patternAssigned;
		this.links = links;
		// TODO links
	}

	public PatternHole() {
		super();
	}

	public PatternHole(Hole hole, String patternParent, String patternAssigned) {
		super(hole.getName(), hole.getData_in(), hole.getData_out(), hole.getEvent_start(), hole.getEvent_end());
		this.patternParent = patternParent;
		this.patternAssigned = patternAssigned;
		// TODO links
	}

	@JsonGetter("pattern_parent")
	public String getPatternParent() {
		return patternParent;
	}

	@JsonSetter("pattern_parent")
	@XmlElement(name = "pattern_parent")
	public void setPatternParent(String patternParent) {
		this.patternParent = patternParent;
	}

	@JsonGetter("pattern_assigned")
	public String getPatternAssigned() {
		return patternAssigned;
	}

	@JsonSetter("pattern_assigned")
	@XmlElement(name = "pattern_assigned")
	public void setPatternAssigned(String patternAssigned) {
		this.patternAssigned = patternAssigned;
	}

	@JsonGetter("links")
	public List<ActionLink> getLinks() {
		return links;
	}

	@JsonSetter("links")
	@XmlElementWrapper(name="links")
	@XmlElement(name="link")
	public void setLinks(List<ActionLink> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "PatternHole [patternParent=" + patternParent + ", patternAssigned=" + patternAssigned + ", links="
				+ links + ", toString()=" + super.toString() + "]";
	}

	public boolean compareWith(Hole obj) {
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
		return true;
	}

}
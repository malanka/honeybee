package services;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@XmlRootElement(name = "hole")
public class PatternHole extends Hole implements Serializable {
	private static final long serialVersionUID = 1L;


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

	public PatternHole(Hole hole, String patternParent) {
		super(hole.getName(), hole.getData_in(), hole.getData_out(), hole.getEvent_start(), hole.getEvent_end());
		this.patternParent = patternParent;
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

	public List<ActionLink> getLinks() {
		return links;
	}
	@XmlElement(name = "links")
	public void setLinks(List<ActionLink> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "PatternHole [patternParent=" + patternParent + ", patternAssigned=" + patternAssigned + ", links="
				+ links + ", toString()=" + super.toString() + "]";
	}

}
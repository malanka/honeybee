package services;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template")
public class Template implements Serializable {
	// TODO move it somewhere
	private static final String base = "http://BP_REST_API/rest";
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;

	private String data_in;
	private String data_out;
	private String event_start;
	private String event_end;

	private List<Hole> holes;

	private List<ActionLink>links;

	private WS ws;

	public Template(){
		super();
	}

	public String getData_in() {
		return data_in;
	}
	@XmlElement
	public void setData_in(String data_in) {
		this.data_in = data_in;
	}

	public String getData_out() {
		return data_out;
	}
	@XmlElement
	public void setData_out(String data_out) {
		this.data_out = data_out;
	}

	public String getEvent_start() {
		return event_start;
	}
	@XmlElement
	public void setEvent_start(String event_start) {
		this.event_start = event_start;
	}

	public String getEvent_end() {
		return event_end;
	}
	@XmlElement
	public void setEvent_end(String event_end) {
		this.event_end = event_end;
	}

	public List<Hole> getHoles() {
		return holes;
	}
	
    @XmlElementWrapper(name="holes")
    @XmlElement(name="hole")
	public void setHoles(List<Hole> holes) {
		this.holes = holes;
	}

	public List<ActionLink> getLinks() {
		return links;
	}
    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
	public void setLinks(List<ActionLink> links) {
		this.links = links;
	}

	public WS getWs() {
		return ws;
	}
	@XmlElement
	public void setWs(WS ws) {
		this.ws = ws;
	}

	public Template(String id, String name, String data_in, String data_out, String event_start, String event_end,
			List<Hole> holes, WS ws) {
		super();
		this.id = id;
		this.name = name;
		this.data_in = data_in;
		this.data_out = data_out;
		this.event_start = event_start;
		this.event_end = event_end;
		this.holes = holes; // TODO holes names should be uniwue
		ActionLink createPattern = new ActionLink("generatepattern", base + "/patterns?template=" + id, "POST");
		this.links = new ArrayList<ActionLink>();
		this.links.add(createPattern);
		this.ws = ws;
	}
	 
	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Template [id=" + id + ", name=" + name + ", data_in=" + data_in + ", data_out=" + data_out
				+ ", event_start=" + event_start + ", event_end=" + event_end + ", holes=" + holes + ", links=" + links
				+ ", ws=" + ws + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Template other = (Template) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

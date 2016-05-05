package services;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pattern")
public class Pattern implements Serializable {
	
	enum Status { WIP, READY };
	// TODO move it somewhere
	private static final String base = "http://BP_REST_API/rest";
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	
	private String templateURI;

	private List<PatternHole> holes;

	private List<ActionLink>links;

	public Pattern(){
		super();
	}

	public List<PatternHole> getHoles() {
		return holes;
	}

	@XmlElement(name="status")
	public Status getStatus(){
		for (PatternHole aHole: holes) {
			if (( aHole.getPatternAssigned() == null ) || ( aHole.getPatternAssigned().isEmpty() ) )
				return Status.WIP;
		}
		return Status.READY;
	}
	
    @XmlElementWrapper(name="holes")
    @XmlElement(name="hole")
	public void setHoles(List<PatternHole> holes) {
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

	public Pattern(String id, String name, String templateURI, List<PatternHole> holes) {
		super();
		this.id = id;
		this.name = name;
		this.holes = holes;
		// TODO
		ActionLink createPattern = new ActionLink("SOMECOMMONLINK","SOMEURI", "POST");
		this.links = new ArrayList<ActionLink>();
		this.links.add(createPattern);
		this.templateURI = templateURI;
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
		Pattern other = (Pattern) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getTemplateURI() {
		return templateURI;
	}
	@XmlElement(name="templateuri")
	public void setTemplateURI(String templateURI) {
		this.templateURI = templateURI;
	}

	@Override
	public String toString() {
		return "Pattern [id=" + id + ", name=" + name + ", templateURI=" + templateURI + ", holes=" + holes + "]";
	}


	

}

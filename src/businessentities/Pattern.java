package businessentities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import services.InstanceService;
import services.PatternService;
import services.TemplateService;

@XmlRootElement(name = "pattern")
public class Pattern implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;

	private String template;

	private List<PatternHole> holes;

	private List<ActionLink>links;

	public Pattern(){
		super();
	}

	@JsonGetter("holes")
	public List<PatternHole> getHoles() {
		return holes;
	}

	@JsonSetter("status")
	@JsonIgnore // This field is read only
	public void setStatus(PatternStatus status) {
	}
		
	
	@XmlElement(name="status")
	@JsonGetter("status")
	public PatternStatus getStatus(){
		if ( holes == null ) {
			return PatternStatus.READY;
		}
		for (PatternHole aHole: holes) {
			if (( aHole.getPatternAssigned() == null ) || ( aHole.getPatternAssigned().isEmpty() ) )
				return PatternStatus.WIP;
		}
		return PatternStatus.READY;
	}

	@JsonIgnore
	@XmlElementWrapper(name="holes")
	@XmlElement(name="hole")
	@JsonSetter("holes")
	public void setHoles(List<PatternHole> holes) {
		this.holes = holes;
	}

	@JsonIgnore
	public void setHolesFromTemplate(List<Hole> holes) {
		if ( holes == null ) {
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

	public Pattern(String id, String name, String template, List<PatternHole> holes) {
		super();
		this.id = id;
		this.name = name;
		this.holes = holes;
		this.template = template;
		generateLinks();
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
	
	@JsonGetter ("template_id")
	public String getTemplate() {
		return template;
	}
	

	public boolean compareWith(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pattern other = (Pattern) obj;
		if (holes == null) {
			if (other.holes != null)
				return false;
		} else if (!holes.equals(other.holes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}

	@JsonSetter ("template_id")
	@XmlElement(name="template_id")
	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "Pattern [id=" + id + ", name=" + name + ", template=" + template + ", holes=" + holes + ", links="
				+ links + "]";
	}

	public void generateLinks() {
		ActionLink getTemplate = new ActionLink("getTemplate",TemplateService.getTemplateURI(this.template), "GET", null);
		ActionLink getHoles = new ActionLink("getHoles",PatternService.getPatternHolesURI(this.id), "GET", null);
		// TODO fix URI
		ActionLink generateInstance = new ActionLink("generateInstance", InstanceService.getInstanceCreationURI(this.id), "POST", null);
		this.links = new ArrayList<ActionLink>();
		this.links.add(getTemplate);
		this.links.add(getHoles);
		this.links.add(generateInstance);
	}

}
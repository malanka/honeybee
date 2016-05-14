package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import services.NotReadyException;

@XmlRootElement(name = "pattern")
public class PatternBasic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8857987920936193916L;

	private String name = null;

	private String templateId = null;
	
	@JsonGetter("template_id")
	public String getTemplateId() {
		return templateId;
	}
	
	@JsonSetter("template_id")
	@XmlElement(name="template_id")
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
		return "PatternBasic [name=" + name + ", templateId=" + templateId + "]";
	}

	public PatternBasic(String name, String templateId) {
		super();
		this.name = name;
		this.templateId = templateId;
	}

	public PatternBasic() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PatternBasic other = (PatternBasic) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public void checkIt() throws NotReadyException {
		if ( name.isEmpty() )
			throw new NotReadyException("'name' has to be specified");
		if ( templateId.isEmpty() )
			throw new NotReadyException("'template_id' has to be specified");
	}

}
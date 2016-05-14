package businessentities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action")
public class ActionLink extends WS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4204705641262839989L;

	private String name;

	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public ActionLink(String name, String uri, String method, String requestDocument) {
		super(uri, method, requestDocument);
		this.name = name;
	}

	public ActionLink() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ActionLink other = (ActionLink) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActionLink [name=" + name + ", toString()=" + super.toString() + "]";
	}
}
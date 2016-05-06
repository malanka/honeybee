package services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action")
public class ActionLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4204705641262839989L;

	private String name;

	private String URI;

	private String method;

	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getURI() {
		return URI;
	}
	@XmlElement
	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getMethod() {
		return method;
	}
	@XmlElement	public void setMethod(String method) {
		this.method = method;
	}

	public ActionLink(String name, String uRI, String method) {
		super();
		this.name = name;
		URI = uRI;
		this.method = method;
	}

	public ActionLink() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((URI == null) ? 0 : URI.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		ActionLink other = (ActionLink) obj;
		if (URI == null) {
			if (other.URI != null)
				return false;
		} else if (!URI.equals(other.URI))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ActionLink [name=" + name + ", URI=" + URI + ", method=" + method + "]";
	}

}

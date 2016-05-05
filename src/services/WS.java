package services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "ws")
public class WS implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String URI;
	
	private String method;

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
	@XmlElement
	public void setMethod(String method) {
		this.method = method;
	}

	public WS(String uRI, String method) {
		super();
		URI = uRI;
		this.method = method;
	}

	public WS() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((URI == null) ? 0 : URI.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		WS other = (WS) obj;
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
		return true;
	}
	@Override
	public String toString() {
		return "WS [URI=" + URI + ", method=" + method + "]";
	}
	
}
